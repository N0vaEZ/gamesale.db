package com.example.gamesaledb.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey
    val gameId: String,
    val title: String,
    val slug: String
)