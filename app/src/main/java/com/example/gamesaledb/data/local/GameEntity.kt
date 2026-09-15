package com.example.gamesaledb.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val slug: String
)