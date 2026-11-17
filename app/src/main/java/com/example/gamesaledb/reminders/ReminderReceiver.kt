package com.example.gamesaledb.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/** Trabalho curto e local; não consulta ITAD nem Firestore. */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(ReminderScheduler.EXTRA_REMINDER_ID, -1)
        if (id <= 0) return
        try {
            ReminderScheduler(context).deliver(id)
        } catch (e: Exception) {
            Log.e("GameReminders", "Não foi possível processar o lembrete $id", e)
        }
    }
}
