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

class WishlistViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val wishlistDao =
        GameSaleDatabase.getDatabase(application).wishlistDao()

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
        slug: String
    ) {
        viewModelScope.launch {
            if (wishlistIds.value.contains(gameId)) {
                wishlistDao.deleteById(gameId)
            } else {
                wishlistDao.insert(
                    WishlistEntity(
                        gameId = gameId,
                        title = title,
                        slug = slug
                    )
                )
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
}