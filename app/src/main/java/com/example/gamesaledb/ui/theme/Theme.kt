package com.example.gamesaledb.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GameSaleDarkColorScheme = darkColorScheme(
    primary = GamerBlue,
    onPrimary = Color.White,

    secondary = GamerCyan,
    onSecondary = Color.Black,

    background = GamerBackground,
    onBackground = GamerText,

    surface = GamerSurface,
    onSurface = GamerText,

    surfaceVariant = GamerSurfaceVariant,
    onSurfaceVariant = GamerTextSecondary,

    error = OfflineRed
)

@Composable
fun GamesaleDBTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = GameSaleDarkColorScheme

    val view = LocalView.current

    if (!view.isInEditMode) {
        val window = (view.context as Activity).window

        WindowCompat.getInsetsController(
            window,
            view
        ).isAppearanceLightStatusBars = false
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}