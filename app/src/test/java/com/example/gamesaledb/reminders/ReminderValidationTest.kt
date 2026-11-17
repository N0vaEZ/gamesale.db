package com.example.gamesaledb.reminders

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ReminderValidationTest {
    private val now = 1_800_000_000_000L

    @Test fun validFutureReminderIsAccepted() {
        assertNull(ReminderValidation.error("game-1", "Conferir Hades", "Ver ofertas", now + 60_000, now))
    }

    @Test fun blankTitleAndMessageAreRejected() {
        for (blank in listOf("", " ", "\n\t")) {
            assertNotNull(ReminderValidation.error("game-1", blank, "Ver ofertas", now + 60_000, now))
            assertNotNull(ReminderValidation.error("game-1", "Conferir Hades", blank, now + 60_000, now))
        }
    }

    @Test fun pastAndExactlyNowAreRejected() {
        for (time in listOf(now - 60_000, now - 1, now)) {
            assertNotNull(ReminderValidation.error("game-1", "Conferir Hades", "Ver ofertas", time, now))
        }
    }

    @Test fun oneMillisecondAfterNowIsTheValidBoundary() {
        // Regra do domínio; o seletor da UI oferece precisão de minutos.
        assertNull(ReminderValidation.error("game-1", "Conferir Hades", "Ver ofertas", now + 1, now))
    }

    @Test fun reminderMustBeLinkedToAGame() {
        assertNotNull(ReminderValidation.error(" ", "Conferir Hades", "Ver ofertas", now + 60_000, now))
    }
}
