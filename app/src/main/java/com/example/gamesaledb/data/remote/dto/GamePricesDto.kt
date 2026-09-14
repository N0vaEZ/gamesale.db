package com.example.gamesaledb.data.remote.dto

data class GamePricesDto(
    val id: String,
    val deals: List<GameDealDto>
)

data class GameDealDto(
    val shop: ShopDto,
    val price: PriceDto,
    val regular: PriceDto?,
    val cut: Int,
    val url: String
)

data class ShopDto(
    val id: Int,
    val name: String
)

data class PriceDto(
    val amount: Double,
    val currency: String
)