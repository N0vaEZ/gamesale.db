package com.example.gamesaledb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameNoteDao {

    @Query("SELECT * FROM game_notes WHERE gameId = :gameId")
    fun getNote(gameId: String): Flow<GameNoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: GameNoteEntity)
}