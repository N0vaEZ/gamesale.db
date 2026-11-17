package com.example.gamesaledb

import android.os.Bundle
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gamesaledb.reminders.ReminderScheduler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gamesaledb.navigation.AppNavigation
import com.example.gamesaledb.ui.theme.GamesaleDBTheme

class MainActivity : ComponentActivity() {
    private var requestedReminderId by mutableStateOf<Int?>(null)

    private fun readReminderIntent(intent: Intent?) {
        requestedReminderId = intent?.getIntExtra(ReminderScheduler.EXTRA_REMINDER_ID, -1)?.takeIf { it > 0 }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        readReminderIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ReminderScheduler(this).createChannel()
        readReminderIntent(intent)

        setContent {
            GamesaleDBTheme {
                AppNavigation(requestedReminderId = requestedReminderId, onReminderHandled = {
                    requestedReminderId = null
                    intent.removeExtra(ReminderScheduler.EXTRA_REMINDER_ID)
                })
            }
        }
    }
}