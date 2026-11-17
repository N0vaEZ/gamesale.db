
package com.example.gamesaledb.data.remote

import com.example.gamesaledb.data.local.WishlistEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseWishlistService {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private suspend fun ensureAuthenticated() {
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }
    }

    suspend fun addWishlistItem(item: WishlistEntity) {
        ensureAuthenticated()

        val data = hashMapOf(
            "gameId" to item.gameId,
            "title" to item.title,
            "slug" to item.slug
        )

        firestore
            .collection("wishlist")
            .document(item.gameId)
            .set(data)
            .await()
    }

    suspend fun deleteWishlistItem(gameId: String) {
        ensureAuthenticated()

        firestore
            .collection("wishlist")
            .document(gameId)
            .delete()
            .await()
    }
}
