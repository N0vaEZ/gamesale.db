package com.example.gamesaledb.reminders

enum class ReminderStatus(val label: String) {
    SCHEDULED("Agendado"),
    PUBLISHED("Enviado ao sistema"),
    CANCELLED("Cancelado"),
    BLOCKED("Notificações bloqueadas no disparo"),
    FAILED("Falha ao agendar ou publicar")
}

data class GameReminder(
    val id: Int,
    val gameId: String,
    val gameTitle: String,
    val gameSlug: String,
    val title: String,
    val message: String,
    val whenMillis: Long,
    val status: ReminderStatus = ReminderStatus.SCHEDULED,
    val publishedAt: Long? = null
)

/** Regras puras: não consulta relógio global, Android, rede ou banco. */
object ReminderValidation {
    fun error(gameId: String, title: String, message: String, whenMillis: Long, now: Long): String? =
        when {
            gameId.isBlank() -> "Selecione um jogo da lista de desejos."
            title.isBlank() -> "Preencha o título."
            message.isBlank() -> "Preencha a mensagem."
            whenMillis <= now -> "Escolha uma data e hora no futuro."
            else -> null
        }
}
