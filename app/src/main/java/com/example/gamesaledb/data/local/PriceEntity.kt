package com.example.gamesaledb.data.local

import androidx.room.Entity

@Entity(
    tableName = "prices",
    primaryKeys = ["gameId", "shopId"]
)
data class PriceEntity(
    val gameId: String,
    val shopId: Int,
    val shopName: String,
    val amount: Double,
    val currency: String
)