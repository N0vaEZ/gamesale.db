package com.example.gamesaledb.data.repository

import com.example.gamesaledb.data.remote.ItadApi
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import com.example.gamesaledb.data.local.GameDao
import com.example.gamesaledb.data.local.GameEntity
import kotlinx.coroutines.flow.Flow
import com.example.gamesaledb.data.local.PriceDao
import com.example.gamesaledb.data.local.PriceEntity
import com.example.gamesaledb.data.remote.dto.GameDealDto
import com.example.gamesaledb.data.remote.dto.PriceDto
import com.example.gamesaledb.data.remote.dto.ShopDto

class GameRepository(
    private val api: ItadApi,
    private val gameDao: GameDao,
    private val priceDao: PriceDao
) {

    suspend fun searchGames(title: String): List<GameSearchDto> {
        return try {
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

            results

        } catch (_: Exception) {

            gameDao.searchCachedGames(title).map { game ->
                GameSearchDto(
                    id = game.id,
                    slug = game.slug,
                    title = game.title,
                    type = null,
                    mature = false
                )
            }
        }
    }

    suspend fun getPrices(gameId: String): List<GameDealDto> {
        return try {
            val result = api.getPrices(
                gameIds = listOf(gameId)
            )

            val deals = result.firstOrNull()?.deals ?: emptyList()

            val priceEntities = deals.map { deal ->
                PriceEntity(
                    gameId = gameId,
                    shopId = deal.shop.id,
                    shopName = deal.shop.name,
                    amount = deal.price.amount,
                    currency = deal.price.currency
                )
            }

            priceDao.deletePricesForGame(gameId)
            priceDao.insertPrices(priceEntities)

            deals

        } catch (_: Exception) {

            val cachedPrices =
                priceDao.getPricesForGame(gameId)

            cachedPrices.map { cached ->
                GameDealDto(
                    shop = ShopDto(
                        id = cached.shopId,
                        name = cached.shopName
                    ),
                    price = PriceDto(
                        amount = cached.amount,
                        currency = cached.currency
                    ),
                    regular = null,
                    cut = 0,
                    url = ""
                )
            }
        }
    }

    fun getCachedGames(): Flow<List<GameEntity>> {
        return gameDao.getAllGames()
    }
}