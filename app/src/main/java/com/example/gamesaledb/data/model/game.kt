package com.example.gamesaledb.data.model

data class GamePrice(
    val store: String,
    val price: Double
)

data class Game(
    val id: String,
    val name: String,
    val prices: List<GamePrice>
)