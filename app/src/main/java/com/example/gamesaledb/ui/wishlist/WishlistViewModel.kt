package com.example.gamesaledb.ui.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesaledb.data.local.GameSaleDatabase
import com.example.gamesaledb.data.local.WishlistEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.gamesaledb.data.local.SyncStatus
import com.example.gamesaledb.data.remote.FirebaseWishlistService
import com.example.gamesaledb.data.local.SyncOperation

class WishlistViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val wishlistDao =
        GameSaleDatabase.getDatabase(application).wishlistDao()

    private val firebaseWishlistService =
        FirebaseWishlistService()

    val wishlistIds: StateFlow<List<String>> =
        wishlistDao.getAll()
            .map { items ->
                items.map { it.gameId }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun toggleWishlist(
        gameId: String,
        title: String,
        slug: String,
        isOnline: Boolean
    ) {
        viewModelScope.launch {
            if (wishlistIds.value.contains(gameId)) {
                wishlistDao.markForDeletion(gameId)
            } else {
                wishlistDao.insert(
                    WishlistEntity(
                        gameId = gameId,
                        title = title,
                        slug = slug,
                        syncStatus = SyncStatus.PENDING.name,
                        syncOperation = SyncOperation.ADD.name
                    )
                )
            }

            if (isOnline) {
                syncPendingWishlist()
            }
        }
    }

    val wishlistItems: StateFlow<List<WishlistEntity>> =
        wishlistDao.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun syncPendingWishlist() {
        viewModelScope.launch {

            val pendingItems = wishlistDao.getPendingItems()

            pendingItems.forEach { item ->

                try {
                    // PENDING → SYNCING
                    wishlistDao.updateSyncStatus(
                        gameId = item.gameId,
                        status = SyncStatus.SYNCING.name
                    )

                    when (item.syncOperation) {

                        SyncOperation.ADD.name -> {
                            firebaseWishlistService.addWishlistItem(item)

                            // Firestore confirmed the ADD
                            wishlistDao.updateSyncStatus(
                                gameId = item.gameId,
                                status = SyncStatus.SYNCED.name
                            )
                        }

                        SyncOperation.DELETE.name -> {
                            firebaseWishlistService.deleteWishlistItem(
                                item.gameId
                            )

                            // Firestore confirmed deletion.
                            // We no longer need the local tombstone.
                            wishlistDao.deleteById(item.gameId)
                        }
                    }

                } catch (e: Exception) {

                    // Keep the operation queued so we can retry later.
                    wishlistDao.updateSyncStatus(
                        gameId = item.gameId,
                        status = SyncStatus.PENDING.name
                    )

                    e.printStackTrace()
                }
            }
        }
    }
}