package com.example.gamesaledb.data.repository

import com.example.gamesaledb.data.remote.ItadApi
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import com.example.gamesaledb.data.remote.dto.GamePricesDto

class GameRepository(
    private val api: ItadApi
) {

    suspend fun searchGames(title: String): List<GameSearchDto> {
        return api.searchGames(title)
    }

    suspend fun getPrices(gameId: String): List<GamePricesDto> {
        return api.getPrices(
            gameIds = listOf(gameId)
        )
    }
}