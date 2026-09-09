package com.example.gamesaledb.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamesaledb.data.model.Game

@Composable
fun GameDetailsScreen(game: Game) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = game.name)

        game.prices.forEach { price ->
            Text(
                text = "${price.store}: R$ ${price.price}"
            )
        }
    }
}