package com.example.gamesaledb.data.repository

import com.example.gamesaledb.data.remote.ItadApi
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import com.example.gamesaledb.data.remote.dto.GamePricesDto
import com.example.gamesaledb.data.local.GameDao
import com.example.gamesaledb.data.local.GameEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val api: ItadApi,
    private val gameDao: GameDao
) {

    suspend fun searchGames(title: String): List<GameSearchDto> {
        val results = api.searchGames(title)

        val entities = results.map { game ->
            GameEntity(
                id = game.id,
                title = game.title,
                slug = game.slug
            )
        }

        gameDao.deleteAllGames()
        gameDao.insertGames(entities)

        return results
    }

    suspend fun getPrices(gameId: String): List<GamePricesDto> {
        return api.getPrices(
            gameIds = listOf(gameId)
        )
    }

    fun getCachedGames(): Flow<List<GameEntity>> {
        return gameDao.getAllGames()
    }
}