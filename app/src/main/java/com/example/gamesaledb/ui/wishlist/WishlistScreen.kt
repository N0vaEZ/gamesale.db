package com.example.gamesaledb.ui.wishlist

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
import com.example.gamesaledb.data.local.WishlistEntity

@Composable
fun WishlistScreen(
    games: List<WishlistEntity>,
    onGameClick: (WishlistEntity) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(games) { game ->
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
                    Text(text = game.title)

                    Text(
                        text = when (game.syncStatus) {
                            "PENDING" -> "⏳ Pending synchronization"
                            "SYNCING" -> "🔄 Synchronizing..."
                            "SYNCED" -> "✓ Synchronized"
                            else -> game.syncStatus
                        }
                    )
                }
            }
        }
    }
}