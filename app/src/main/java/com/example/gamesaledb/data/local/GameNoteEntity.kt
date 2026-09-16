package com.example.gamesaledb.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_notes")
data class GameNoteEntity(
    @PrimaryKey
    val gameId: String,
    val note: String
)