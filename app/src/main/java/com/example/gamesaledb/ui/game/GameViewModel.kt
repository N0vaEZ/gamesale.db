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

    private val repository = GameRepository(
        api = RetrofitClient.api,
        gameDao = GameSaleDatabase
            .getDatabase(application)
            .gameDao()
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
                val result = repository.getPrices(gameId)

                println("PRICE RESULT SIZE: ${result.size}")

                result.forEach { gamePrices ->
                    println("GAME ID: ${gamePrices.id}")
                    println("DEALS COUNT: ${gamePrices.deals.size}")

                    gamePrices.deals.forEach { deal ->
                        println(
                            "${deal.shop.name}: " +
                                    "${deal.price.currency} ${deal.price.amount}"
                        )
                    }
                }

                _prices.value =
                    result.firstOrNull()?.deals ?: emptyList()

            } catch (e: Exception) {
                println("PRICE REQUEST FAILED")
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