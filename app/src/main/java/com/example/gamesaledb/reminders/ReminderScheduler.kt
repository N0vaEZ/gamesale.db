package com.example.gamesaledb.reminders

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.gamesaledb.MainActivity
import com.example.gamesaledb.R

class ReminderScheduler(context: Context) {
    private val context = context.applicationContext
    private val store = ReminderStore(this.context)
    private val alarms = this.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(NotificationChannel(
                CHANNEL_ID, "Lembretes de jogos", NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Lembretes para conferir jogos da sua lista de desejos" })
        }
    }

    fun notificationsAllowed(): Boolean {
        createChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED) return false
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = context.getSystemService(NotificationManager::class.java)
                .getNotificationChannel(CHANNEL_ID)
            if (channel?.importance == NotificationManager.IMPORTANCE_NONE) return false
        }
        return true
    }

    fun settingsIntent(): Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    } else {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.packageName}"))
    }

    fun schedule(gameId: String, gameTitle: String, gameSlug: String, title: String,
                 message: String, whenMillis: Long): GameReminder = synchronized(ReminderStore.lock) {
        val error = ReminderValidation.error(gameId, title, message, whenMillis, System.currentTimeMillis())
        require(error == null) { error ?: "Dados inválidos." }
        check(notificationsAllowed()) { "Ative as notificações do aplicativo e do canal de lembretes." }
        val reminder = store.add(gameId, gameTitle, gameSlug, title, message, whenMillis)
        try {
            register(reminder)
        } catch (e: Exception) {
            store.update(reminder.copy(status = ReminderStatus.FAILED))
            throw e
        }
        reminder
    }

    private fun alarmIntent(id: Int, flags: Int): PendingIntent? = PendingIntent.getBroadcast(
        context, id, Intent(context, ReminderReceiver::class.java)
            .setAction(ACTION_DELIVER).putExtra(EXTRA_REMINDER_ID, id),
        flags or PendingIntent.FLAG_IMMUTABLE
    )

    private fun register(reminder: GameReminder) {
        alarms.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.whenMillis,
            checkNotNull(alarmIntent(reminder.id, PendingIntent.FLAG_UPDATE_CURRENT)))
    }

    fun cancel(id: Int): Boolean = synchronized(ReminderStore.lock) {
        val item = store.find(id) ?: return false
        if (item.status != ReminderStatus.SCHEDULED) return false
        // O receiver consulta o estado persistido, inclusive se o evento já estiver na fila.
        store.update(item.copy(status = ReminderStatus.CANCELLED))
        alarmIntent(id, PendingIntent.FLAG_NO_CREATE)?.let {
            alarms.cancel(it)
            it.cancel()
        }
        NotificationManagerCompat.from(context).cancel(id)
        true
    }

    fun deliver(id: Int) = synchronized(ReminderStore.lock) {
        val item = store.find(id) ?: return
        if (item.status != ReminderStatus.SCHEDULED) return
        if (System.currentTimeMillis() < item.whenMillis) {
            register(item)
            return
        }
        if (!notificationsAllowed()) {
            store.update(item.copy(status = ReminderStatus.BLOCKED))
            return
        }
        try {
            publish(item.id, item.title, item.message, item.id)
            store.update(item.copy(status = ReminderStatus.PUBLISHED,
                publishedAt = System.currentTimeMillis()))
        } catch (e: SecurityException) {
            store.update(item.copy(status = ReminderStatus.BLOCKED))
        } catch (e: Exception) {
            store.update(item.copy(status = ReminderStatus.FAILED))
            throw e
        }
    }

    fun testNow() {
        check(notificationsAllowed()) { "Ative as notificações e o canal de lembretes." }
        publish(0, "Teste do GameSaleDB", "O canal funciona. Este teste não verifica o agendamento.", null)
    }

    @SuppressLint("MissingPermission") // Os chamadores conferem a permissão; a revogação é tratada.
    private fun publish(id: Int, title: String, message: String, reminderId: Int?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            if (reminderId != null) putExtra(EXTRA_REMINDER_ID, reminderId)
        }
        val openApp = PendingIntent.getActivity(context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_reminder)
            .setContentTitle(title).setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(openApp).setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(id, notification)
    }

    companion object {
        const val CHANNEL_ID = "game_reminders_v1"
        const val EXTRA_REMINDER_ID = "com.example.gamesaledb.REMINDER_ID"
        private const val ACTION_DELIVER = "com.example.gamesaledb.DELIVER_REMINDER"
    }
}
