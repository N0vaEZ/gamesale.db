package com.example.gamesaledb.ui.game

import androidx.lifecycle.viewModelScope
import com.example.gamesaledb.data.remote.RetrofitClient
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import com.example.gamesaledb.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gamesaledb.data.local.GameSaleDatabase
import com.example.gamesaledb.data.local.GameEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import com.example.gamesaledb.data.local.WishlistEntity

class GameViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        GameSaleDatabase.getDatabase(application)

    private val repository = GameRepository(
        api = RetrofitClient.api,
        gameDao = database.gameDao(),
        priceDao = database.priceDao()
    )

    private val _games =
        MutableStateFlow<List<GameSearchDto>>(emptyList())

    val games: StateFlow<List<GameSearchDto>> =
        _games.asStateFlow()

    fun searchGames(title: String) {
        viewModelScope.launch {
            try {
                _games.value = repository.searchGames(title)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadPrices(gameId: String) {
        viewModelScope.launch {

            _priceUiState.value = GamePriceUiState(
                isLoading = true
            )

            try {
                val result = repository.getPrices(gameId)

                _priceUiState.value = GamePriceUiState(
                    prices = result.prices,
                    isLoading = false,
                    message = if (result.fromCache) {
                        if (result.prices.isEmpty()) {
                            "Unable to load prices."
                        } else {
                            "No internet. Showing saved prices."
                        }
                    } else {
                        "Prices updated successfully."
                    }
                )

            } catch (_: Exception) {
                _priceUiState.value = GamePriceUiState(
                    isLoading = false,
                    message = "Unable to load prices."
                )
            }
        }
    }
    private val _priceUiState =
        MutableStateFlow(GamePriceUiState())

    val priceUiState: StateFlow<GamePriceUiState> =
        _priceUiState.asStateFlow()

    val cachedGames: StateFlow<List<GameEntity>> =
        repository.getCachedGames()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val _selectedGame =
        MutableStateFlow<GameSearchDto?>(null)

    val selectedGame: StateFlow<GameSearchDto?> =
        _selectedGame.asStateFlow()

    fun selectGame(game: GameSearchDto) {
        _selectedGame.value = game
    }

    fun selectCachedGame(game: GameEntity) {
        _selectedGame.value = GameSearchDto(
            id = game.id,
            slug = game.slug,
            title = game.title,
            type = null,
            mature = false
        )
    }

    fun selectWishlistGame(game: WishlistEntity) {
        _selectedGame.value = GameSearchDto(
            id = game.gameId,
            slug = game.slug,
            title = game.title,
            type = null,
            mature = false
        )
    }
}