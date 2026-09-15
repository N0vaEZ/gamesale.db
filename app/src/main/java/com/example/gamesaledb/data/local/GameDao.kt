package com.example.gamesaledb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM games")
    fun getAllGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("DELETE FROM games")
    suspend fun deleteAllGames()

    @Query("""
    SELECT * FROM games
    WHERE title LIKE '%' || :title || '%'
""")
    suspend fun searchCachedGames(title: String): List<GameEntity>
}