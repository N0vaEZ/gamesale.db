package com.example.gamesaledb.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamesaledb.ui.game.GameListScreen
import com.example.gamesaledb.ui.wishlist.WishlistScreen
import com.example.gamesaledb.ui.wishlist.WishlistViewModel
import com.example.gamesaledb.ui.game.GameViewModel
import com.example.gamesaledb.ui.game.ApiGameDetailsScreen

@Composable
fun AppNavigation() {
    val gameViewModel: GameViewModel = viewModel()
    val apiGames by gameViewModel.games.collectAsState()
    val navController = rememberNavController()
    val wishlistViewModel: WishlistViewModel = viewModel()
    val priceUiState by gameViewModel.priceUiState.collectAsState()
    val cachedGames by gameViewModel.cachedGames.collectAsState()
    val selectedGame by gameViewModel.selectedGame.collectAsState()
    val wishlistIds by wishlistViewModel.wishlistIds.collectAsState()
    val wishlistItems by wishlistViewModel.wishlistItems.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "games"
    ) {

        composable("games") {
            Column {
                Button(
                    onClick = {
                        navController.navigate("wishlist")
                    }
                ) {
                    Text("Wishlist")
                }

                GameListScreen(
                    apiGames = apiGames,
                    cachedGames = cachedGames,
                    onSearch = { title ->
                        gameViewModel.searchGames(title)
                    },
                    onApiGameClick = { game ->
                        gameViewModel.selectGame(game)
                        gameViewModel.loadPrices(game.id)

                        navController.navigate("apiGameDetails")
                    },
                    onCachedGameClick = { game ->
                        gameViewModel.selectCachedGame(game)
                        gameViewModel.loadPrices(game.id)

                        navController.navigate("apiGameDetails")
                    },
                    )
            }
        }

        composable("wishlist") {
            WishlistScreen(
                games = wishlistItems,
                onGameClick = { game ->
                    gameViewModel.selectWishlistGame(game)
                    gameViewModel.loadPrices(game.gameId)
                    navController.navigate("apiGameDetails")
                }
            )
        }

        composable("apiGameDetails") {
            selectedGame?.let { game ->
                ApiGameDetailsScreen(
                    game = game,
                    prices = priceUiState.prices,
                    isLoading = priceUiState.isLoading,
                    message = priceUiState.message,
                    isWishlisted = wishlistIds.contains(game.id),
                    onWishlistClick = {
                        wishlistViewModel.toggleWishlist(
                            gameId = game.id,
                            title = game.title,
                            slug = game.slug
                        )
                    }
                )
            }
        }
    }

}