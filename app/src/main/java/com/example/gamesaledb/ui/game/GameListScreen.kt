package com.example.gamesaledb.ui.game

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamesaledb.data.local.GameEntity
import com.example.gamesaledb.data.remote.dto.GameSearchDto

@Composable
fun GameListScreen(
    apiGames: List<GameSearchDto>,
    onSearch: (String) -> Unit,
    onApiGameClick: (GameSearchDto) -> Unit,
    cachedGames: List<GameEntity>,
    onCachedGameClick: (GameEntity) -> Unit,
    isOnline: Boolean,
) {
    Text(
        text = if (isOnline) {
            "🟢 Online"
        } else {
            "🔴 Offline"
        },
        modifier = Modifier.padding(16.dp)
    )

    var searchText by remember {
        mutableStateOf("")
    }

    var showSearchResults by remember {
        mutableStateOf(false)
    }
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row {
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
                        showSearchResults = true
                    }
                ) {
                    Text("Search")
                }
            }
        }

        if (showSearchResults) {

            items(apiGames) { game ->
                Card(
                    onClick = {
                        showSearchResults = false
                        onApiGameClick(game)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = game.title,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (cachedGames.isNotEmpty()) {
            item {
                Text(
                    text = "Cached games",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(cachedGames) { game ->
                Card(
                    onClick = {
                        onCachedGameClick(game)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = game.title,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}