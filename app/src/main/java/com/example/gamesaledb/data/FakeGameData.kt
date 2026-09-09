package com.example.gamesaledb.data

import com.example.gamesaledb.data.model.Game
import com.example.gamesaledb.data.model.GamePrice

val fakeGames = listOf(
    Game(
        id = "1",
        name = "Cyberpunk 2077",
        prices = listOf(
            GamePrice(
                store = "Steam",
                price = 99.99
            ),
            GamePrice(
                store = "GOG",
                price = 89.99
            )
        )
    ),

    Game(
        id = "2",
        name = "The Witcher 3",
        prices = listOf(
            GamePrice(
                store = "Steam",
                price = 39.99
            ),
            GamePrice(
                store = "GOG",
                price = 29.99
            )
        )
    )
)