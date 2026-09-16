package com.example.gamesaledb.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Query("SELECT * FROM wishlist WHERE syncOperation != 'DELETE'")
    fun getAll(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WishlistEntity)

    @Delete
    suspend fun delete(item: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE gameId = :gameId")
    suspend fun deleteById(gameId: String)

    @Query("SELECT * FROM wishlist WHERE syncStatus = 'PENDING'")
    suspend fun getPendingItems(): List<WishlistEntity>

    @Query("UPDATE wishlist SET syncStatus = :status WHERE gameId = :gameId")
    suspend fun updateSyncStatus(
        gameId: String,
        status: String
    )

    @Query(
        """
    UPDATE wishlist
    SET syncStatus = 'PENDING',
        syncOperation = 'DELETE'
    WHERE gameId = :gameId
    """
    )
    suspend fun markForDeletion(gameId: String)
}