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
import com.example.gamesaledb.data.fakeGames
import com.example.gamesaledb.ui.game.GameDetailsScreen
import com.example.gamesaledb.ui.game.GameListScreen
import com.example.gamesaledb.ui.wishlist.WishlistScreen
import com.example.gamesaledb.ui.wishlist.WishlistViewModel
import com.example.gamesaledb.ui.game.GameViewModel

@Composable
fun AppNavigation() {
    val gameViewModel: GameViewModel = viewModel()
    val apiGames by gameViewModel.games.collectAsState()
    val navController = rememberNavController()
    val wishlistViewModel: WishlistViewModel = viewModel()
    val apiPrices by gameViewModel.prices.collectAsState()
    val cachedGames by gameViewModel.cachedGames.collectAsState()

    val wishlistIds by wishlistViewModel
        .wishlistIds
        .collectAsState()

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
                    onGameClick = { game ->
                        navController.navigate("game/${game.id}")
                    },
                    apiGames = apiGames,
                    apiPrices = apiPrices,
                    cachedGames = cachedGames,
                    onSearch = { title ->
                        gameViewModel.searchGames(title)
                    },
                    onApiGameClick = { game ->
                        gameViewModel.loadPrices(game.id)
                    },
                    onCachedGameClick = { game ->
                        gameViewModel.loadPrices(game.id)
                    },
                    )
            }
        }

        composable("game/{gameId}") { backStackEntry ->

            val gameId = backStackEntry.arguments?.getString("gameId")

            val game = fakeGames.find {
                it.id == gameId
            }

            if (game != null) {
                GameDetailsScreen(
                    game = game,
                    isWishlisted = wishlistIds.contains(game.id),
                    onWishlistClick = {
                        wishlistViewModel.toggleWishlist(game.id)
                    }
                )
            }
        }

        composable("wishlist") {
            val wishlistGames = fakeGames.filter { game ->
                wishlistIds.contains(game.id)
            }

            WishlistScreen(
                games = wishlistGames,
                onGameClick = { game ->
                    navController.navigate("game/${game.id}")
                }
            )
        }
    }

}