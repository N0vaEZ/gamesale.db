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
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.gamesaledb.util.NetworkMonitor
import androidx.compose.runtime.LaunchedEffect
import com.example.gamesaledb.ui.game.GameNoteViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val context = LocalContext.current

    val networkMonitor = remember {
        NetworkMonitor(context.applicationContext)
    }

    val isOnline by networkMonitor.isOnline.collectAsState()
    val gameViewModel: GameViewModel = viewModel()
    val apiGames by gameViewModel.games.collectAsState()
    val navController = rememberNavController()
    val wishlistViewModel: WishlistViewModel = viewModel()
    val priceUiState by gameViewModel.priceUiState.collectAsState()
    val cachedGames by gameViewModel.cachedGames.collectAsState()
    val selectedGame by gameViewModel.selectedGame.collectAsState()
    val wishlistIds by wishlistViewModel.wishlistIds.collectAsState()
    val wishlistItems by wishlistViewModel.wishlistItems.collectAsState()
    val gameNoteViewModel: GameNoteViewModel = viewModel()
    val gameNote by gameNoteViewModel.note.collectAsState()

    LaunchedEffect(isOnline) {
        if (isOnline) {
            wishlistViewModel.syncPendingWishlist()
        }
    }


    NavHost(
        navController = navController,
        startDestination = "games"
    ) {

        composable("games") {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("GameSaleDB")
                        },
                        actions = {
                            Button(
                                onClick = {
                                    navController.navigate("wishlist")
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Lista de Desejos")
                            }
                        }
                    )
                }
            ) { innerPadding ->

                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    GameListScreen(
                        apiGames = apiGames,
                        cachedGames = cachedGames,
                        isOnline = isOnline,

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
                        }
                    )
                }
            }
        }

        composable("wishlist") {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("Lista de Desejos")
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Voltar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->

                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    WishlistScreen(
                        games = wishlistItems,
                        onGameClick = { game ->
                            gameViewModel.selectWishlistGame(game)
                            gameViewModel.loadPrices(game.gameId)
                            navController.navigate("apiGameDetails")
                        }
                    )
                }
            }
        }

        composable("apiGameDetails") {
            selectedGame?.let { game ->

                LaunchedEffect(game.id) {
                    gameNoteViewModel.selectGame(game.id)
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Detalhes do Jogo")
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        navController.popBackStack()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Voltar",
                                        tint = MaterialTheme.colorScheme.primary

                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->

                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
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
                                    slug = game.slug,
                                    isOnline = isOnline
                                )
                            },

                            savedNote = gameNote?.note ?: "",

                            onSaveNote = { note ->
                                gameNoteViewModel.saveNote(
                                    gameId = game.id,
                                    note = note
                                )
                            }
                        )
                    }
                }
            }
        }
    }

}