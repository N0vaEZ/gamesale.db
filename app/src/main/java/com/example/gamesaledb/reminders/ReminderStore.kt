package com.example.gamesaledb.reminders

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Lista pequena, local, sem dados de autenticação. Não substitui o banco Room. */
class ReminderStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(
        "game_reminders", Context.MODE_PRIVATE
    )

    fun all(): List<GameReminder> = synchronized(lock) {
        val array = JSONArray(prefs.getString("items", "[]") ?: "[]")
        (0 until array.length()).map { index ->
            val o = array.getJSONObject(index)
            GameReminder(
                id = o.getInt("id"), gameId = o.getString("gameId"),
                gameTitle = o.getString("gameTitle"), gameSlug = o.getString("gameSlug"),
                title = o.getString("title"), message = o.getString("message"),
                whenMillis = o.getLong("whenMillis"),
                status = ReminderStatus.valueOf(o.getString("status")),
                publishedAt = if (o.isNull("publishedAt")) null else o.getLong("publishedAt")
            )
        }.sortedBy { it.whenMillis }
    }

    fun find(id: Int): GameReminder? = all().firstOrNull { it.id == id }

    fun add(gameId: String, gameTitle: String, gameSlug: String, title: String,
            message: String, whenMillis: Long): GameReminder = synchronized(lock) {
        val items = all()
        val lastId = maxOf(prefs.getInt("lastId", 0), items.maxOfOrNull { it.id } ?: 0)
        check(lastId < Int.MAX_VALUE) { "Limite de identificadores atingido." }
        val item = GameReminder(lastId + 1, gameId, gameTitle, gameSlug,
            title.trim(), message.trim(), whenMillis)
        persist(items + item, item.id)
        item
    }

    fun update(item: GameReminder) = synchronized(lock) {
        val items = all()
        check(items.any { it.id == item.id }) { "Lembrete não encontrado." }
        persist(items.map { if (it.id == item.id) item else it }, prefs.getInt("lastId", item.id))
    }

    private fun persist(items: List<GameReminder>, lastId: Int) {
        val array = JSONArray()
        items.forEach { item ->
            array.put(JSONObject().apply {
                put("id", item.id); put("gameId", item.gameId)
                put("gameTitle", item.gameTitle); put("gameSlug", item.gameSlug)
                put("title", item.title); put("message", item.message)
                put("whenMillis", item.whenMillis); put("status", item.status.name)
                put("publishedAt", item.publishedAt ?: JSONObject.NULL)
            })
        }
        check(prefs.edit().putString("items", array.toString()).putInt("lastId", lastId).commit()) {
            "Não foi possível salvar os lembretes neste dispositivo."
        }
    }

    companion object {
        // Serializa também agendamento, cancelamento e receiver no mesmo processo.
        internal val lock = Any()
    }
}
