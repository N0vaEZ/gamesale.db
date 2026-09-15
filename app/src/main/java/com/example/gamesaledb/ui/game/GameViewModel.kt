package com.example.gamesaledb.ui.game

import androidx.lifecycle.viewModelScope
import com.example.gamesaledb.data.remote.RetrofitClient
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import com.example.gamesaledb.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.gamesaledb.data.remote.dto.GameDealDto
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gamesaledb.data.local.GameSaleDatabase
import com.example.gamesaledb.data.local.GameEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

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
            try {
                _prices.value = repository.getPrices(gameId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private val _prices =
        MutableStateFlow<List<GameDealDto>>(emptyList())

    val prices: StateFlow<List<GameDealDto>> =
        _prices.asStateFlow()

    val cachedGames: StateFlow<List<GameEntity>> =
        repository.getCachedGames()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}