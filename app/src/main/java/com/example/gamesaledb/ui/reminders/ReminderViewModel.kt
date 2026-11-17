package com.example.gamesaledb.ui.reminders

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesaledb.data.local.GameSaleDatabase
import com.example.gamesaledb.data.local.WishlistEntity
import com.example.gamesaledb.reminders.GameReminder
import com.example.gamesaledb.reminders.ReminderScheduler
import com.example.gamesaledb.reminders.ReminderStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ReminderUiState(
    val items: List<GameReminder> = emptyList(),
    val notificationsAllowed: Boolean = false,
    val busy: Boolean = false,
    val message: String? = null
)

class ReminderViewModel(application: Application) : AndroidViewModel(application) {
    private val store = ReminderStore(application)
    private val scheduler = ReminderScheduler(application)
    private val prefs = application.getSharedPreferences("game_reminders", Application.MODE_PRIVATE)
    private val _state = MutableStateFlow(ReminderUiState())
    val state = _state.asStateFlow()
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> refresh() }

    init {
        prefs.registerOnSharedPreferenceChangeListener(listener)
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val (items, allowed) = withContext(Dispatchers.IO) {
                    store.all() to scheduler.notificationsAllowed()
                }
                _state.update { it.copy(items = items, notificationsAllowed = allowed) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                inform("Não foi possível ler os lembretes. Os dados existentes foram preservados.")
            }
        }
    }

    fun inform(message: String) { _state.update { it.copy(message = message) } }

    fun schedule(game: WishlistEntity, title: String, message: String, whenMillis: Long) =
        execute("Lembrete agendado. O horário é aproximado e o Android pode atrasar a entrega.") {
            val currentWishlist = GameSaleDatabase.getDatabase(getApplication()).wishlistDao().getAll().first()
            check(currentWishlist.any { it.gameId == game.gameId }) { "Este jogo não está mais na lista de desejos." }
            scheduler.schedule(game.gameId, game.title, game.slug, title, message, whenMillis)
        }

    fun cancel(id: Int) = execute("Lembrete cancelado; o histórico foi preservado.") {
        check(scheduler.cancel(id)) { "Este lembrete não está mais agendado. Atualize a lista." }
    }

    fun testNow() = execute("Notificação de teste enviada ao sistema. Isso não valida o agendamento futuro.") {
        scheduler.testNow()
    }

    private fun execute(success: String, action: suspend () -> Unit) {
        if (_state.value.busy) return
        _state.update { it.copy(busy = true, message = null) }
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { action() }
                inform(success)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                inform(e.message ?: "Não foi possível concluir a operação. Tente novamente.")
            } finally {
                _state.update { it.copy(busy = false) }
                refresh()
            }
        }
    }

    override fun onCleared() {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
        super.onCleared()
    }
}
