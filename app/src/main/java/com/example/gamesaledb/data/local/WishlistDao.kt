package com.example.gamesaledb.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Query("SELECT * FROM wishlist")
    fun getAll(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: WishlistEntity)

    @Delete
    suspend fun delete(item: WishlistEntity)
}