package com.example.gamesaledb

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameListScreen() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(fakeGames) { game ->
            Card(
                onClick = {
                    println("Clicked ${game.name}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
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
        }
    }
}