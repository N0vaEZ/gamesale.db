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
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import com.example.gamesaledb.data.remote.dto.GameDealDto
import com.example.gamesaledb.data.local.GameEntity

@Composable
fun GameListScreen(
    onGameClick: (Game) -> Unit,
    apiGames: List<GameSearchDto>,
    onSearch: (String) -> Unit,
    onApiGameClick: (GameSearchDto) -> Unit,
    apiPrices: List<GameDealDto>,
    cachedGames: List<GameEntity>
) {
    var searchText by remember {
        mutableStateOf("")
    }

    Column {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                label = {
                    Text("Search games")
                }
            )

            Button(
                onClick = {
                    onSearch(searchText)
                }
            ) {
                Text("Search")
            }
        }

        apiGames.forEach { game ->
            Card(
                onClick = {
                    onApiGameClick(game)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    )
            ) {
                Text(
                    text = game.title,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        apiPrices.forEach { deal ->
            Text(
                text = "${deal.shop.name}: ${deal.price.currency} ${deal.price.amount}",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        if (cachedGames.isNotEmpty()) {
            Text(
                text = "Cached games",
                modifier = Modifier.padding(16.dp)
            )

            cachedGames.forEach { game ->
                Text(
                    text = game.title,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
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