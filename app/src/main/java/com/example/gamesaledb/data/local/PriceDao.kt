package com.example.gamesaledb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PriceDao {

    @Query("SELECT * FROM prices WHERE gameId = :gameId")
    suspend fun getPricesForGame(gameId: String): List<PriceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<PriceEntity>)

    @Query("DELETE FROM prices WHERE gameId = :gameId")
    suspend fun deletePricesForGame(gameId: String)
}