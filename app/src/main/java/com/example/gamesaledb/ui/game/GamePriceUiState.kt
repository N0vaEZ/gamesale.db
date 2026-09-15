package com.example.gamesaledb.ui.game

import com.example.gamesaledb.data.remote.dto.GameDealDto

data class GamePriceUiState(
    val prices: List<GameDealDto> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)