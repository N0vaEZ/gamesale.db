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

@Composable
fun ApiGameDetailsScreen(
    game: GameSearchDto,
    prices: List<GameDealDto>,
    isWishlisted: Boolean,
    onWishlistClick: () -> Unit
) {
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