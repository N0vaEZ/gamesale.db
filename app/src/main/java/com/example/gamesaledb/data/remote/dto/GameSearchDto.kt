package com.example.gamesaledb.data.remote.dto

data class GameSearchDto(
    val id: String,
    val slug: String,
    val title: String,
    val type: String?,
    val mature: Boolean
)