package com.example.gamesaledb.ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesaledb.data.local.GameNoteEntity
import com.example.gamesaledb.data.local.GameSaleDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class GameNoteViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val gameNoteDao =
        GameSaleDatabase.getDatabase(application).gameNoteDao()

    private val _gameId =
        kotlinx.coroutines.flow.MutableStateFlow<String?>(null)

    val note: StateFlow<GameNoteEntity?> =
        _gameId

            .flatMapLatest { gameId ->
                if (gameId == null) {
                    flowOf(null)
                } else {
                    gameNoteDao.getNote(gameId)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    fun selectGame(gameId: String) {
        _gameId.value = gameId
    }

    fun saveNote(gameId: String, note: String) {
        viewModelScope.launch {
            gameNoteDao.saveNote(
                GameNoteEntity(
                    gameId = gameId,
                    note = note
                )
            )
        }
    }
}