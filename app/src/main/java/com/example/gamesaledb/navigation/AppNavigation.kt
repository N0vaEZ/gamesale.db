package com.example.gamesaledb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamesaledb.data.fakeGames
import com.example.gamesaledb.ui.game.GameDetailsScreen
import com.example.gamesaledb.ui.game.GameListScreen
import com.example.gamesaledb.ui.wishlist.WishlistScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.gamesaledb.data.local.GameSaleDatabase
import com.example.gamesaledb.data.local.WishlistEntity

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val database = GameSaleDatabase.getDatabase(context)
    val wishlistDao = database.wishlistDao()

    val wishlistItems by wishlistDao
        .getAll()
        .collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()
    val wishlistIds = wishlistItems.map { it.gameId }

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
                    }
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
                        scope.launch {
                            if (wishlistIds.contains(game.id)) {
                                wishlistDao.delete(
                                    WishlistEntity(gameId = game.id)
                                )
                            } else {
                                wishlistDao.insert(
                                    WishlistEntity(gameId = game.id)
                                )
                            }
                        }
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