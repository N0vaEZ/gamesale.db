package com.example.gamesaledb

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameListScreen() {
    LazyColumn {
        items(fakeGames) { game ->
            Column {
                Text(text = game.name)

                game.prices.forEach { price ->
                    Text(
                        text = "${price.store}: R$ ${price.price}"
                    )
                }
            }
        }
    }
}