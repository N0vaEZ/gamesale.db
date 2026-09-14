package com.example.gamesaledb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamesaledb.data.fakeGames
import com.example.gamesaledb.ui.game.GameDetailsScreen
import com.example.gamesaledb.ui.game.GameListScreen
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.gamesaledb.ui.wishlist.WishlistScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val wishlist = remember {
        mutableStateListOf<String>()
    }

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
                    isWishlisted = wishlist.contains(game.id),
                    onWishlistClick = {
                        if (wishlist.contains(game.id)) {
                            wishlist.remove(game.id)
                        } else {
                            wishlist.add(game.id)
                        }
                    }
                )
            }
        }

        composable("wishlist") {
            val wishlistGames = fakeGames.filter { game ->
                wishlist.contains(game.id)
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