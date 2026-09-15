package com.example.gamesaledb.data.repository

import com.example.gamesaledb.data.remote.dto.GameDealDto

data class PriceResult(
    val prices: List<GameDealDto>,
    val fromCache: Boolean
)