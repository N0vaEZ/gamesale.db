package com.example.gamesaledb.ui.game


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
import com.example.gamesaledb.data.fakeGames
import com.example.gamesaledb.data.model.Game

@Composable
fun GameListScreen(
    onGameClick: (Game) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(fakeGames) { game ->
            Card(
                onClick = {
                    onGameClick(game)
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