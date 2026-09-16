package com.example.gamesaledb.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamesaledb.data.local.GameEntity
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import com.example.gamesaledb.ui.theme.OnlineGreen
import com.example.gamesaledb.ui.theme.OfflineRed

@Composable
fun GameListScreen(
    apiGames: List<GameSearchDto>,
    onSearch: (String) -> Unit,
    onApiGameClick: (GameSearchDto) -> Unit,
    cachedGames: List<GameEntity>,
    onCachedGameClick: (GameEntity) -> Unit,
    isOnline: Boolean,
) {
    var searchText by remember {
        mutableStateOf("")
    }

    var showSearchResults by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Header
        item {
            Text(
                text = "Encontre jogos e compare preços",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        // Connection status
        item {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 2.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = if (isOnline) {
                                    OnlineGreen
                                } else {
                                    OfflineRed
                                },
                                shape = CircleShape
                            )
                    )

                    Text(
                        text = if (isOnline) {
                            "Online"
                        } else {
                            "Offline"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Search
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    label = {
                        Text("Pesquisar jogos")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (searchText.isNotBlank()) {
                            onSearch(searchText)
                            showSearchResults = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pesquisar")
                }
            }
        }

        // API search results
        if (showSearchResults) {

            item {
                Text(
                    text = "Resultados da pesquisa",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(apiGames) { game ->
                GameResultCard(
                    title = game.title,
                    subtitle = "Ver preços e detalhes",
                    onClick = {
                        showSearchResults = false
                        onApiGameClick(game)
                    }
                )
            }
        }

        // Cached games
        if (cachedGames.isNotEmpty()) {

            item {
                Text(
                    text = if (isOnline) {
                        "Jogos recentes"
                    } else {
                        "Jogos disponíveis offline"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(cachedGames) { game ->
                GameResultCard(
                    title = game.title,
                    subtitle = if (isOnline) {
                        "Salvo localmente"
                    } else {
                        "Disponível offline"
                    },
                    onClick = {
                        onCachedGameClick(game)
                    }
                )
            }
        }
    }
}

@Composable
private fun GameResultCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = "›",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}