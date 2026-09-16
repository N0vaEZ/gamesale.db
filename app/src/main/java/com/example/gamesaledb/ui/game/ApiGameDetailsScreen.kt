package com.example.gamesaledb.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamesaledb.data.remote.dto.GameDealDto
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ApiGameDetailsScreen(
    game: GameSearchDto,
    prices: List<GameDealDto>,
    isLoading: Boolean,
    message: String?,
    isWishlisted: Boolean,
    onWishlistClick: () -> Unit,
    savedNote: String,
    onSaveNote: (String) -> Unit
) {
    var noteText by remember(game.id) {
        mutableStateOf(savedNote)
    }

    var noteSaved by remember(game.id) {
        mutableStateOf(false)
    }

    LaunchedEffect(savedNote) {
        noteText = savedNote
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = game.title,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Button(
                onClick = onWishlistClick,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = if (isWishlisted) {
                        "Remove from Wishlist"
                    } else {
                        "Add to Wishlist"
                    }
                )
            }
        }

        item {
            Text(
                text = "Your Note",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = noteText,
                onValueChange = {
                    noteText = it
                    noteSaved = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                label = {
                    Text("Personal note")
                }
            )

            Button(
                onClick = {
                    onSaveNote(noteText)
                    noteSaved = true
                },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text("Save Note")
            }

            if (noteSaved) {
                Text(
                    text = "✓ Note saved successfully",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        if (isLoading) {
            item {
                Row(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    Text(
                        text = "Synchronizing prices...",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }

        if (message != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "Prices",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(prices) { deal ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = deal.shop.name)

                    Text(
                        text = "${deal.price.currency} ${deal.price.amount}"
                    )
                }
            }
        }
    }
}