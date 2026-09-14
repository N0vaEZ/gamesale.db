package com.example.gamesaledb.data.remote

import com.example.gamesaledb.BuildConfig
import com.example.gamesaledb.data.remote.dto.GameSearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ItadApi {

    @GET("games/search/v1")
    suspend fun searchGames(
        @Query("title") title: String,
        @Query("results") results: Int = 10,
        @Query("key") apiKey: String = BuildConfig.ITAD_API_KEY
    ): List<GameSearchDto>
}