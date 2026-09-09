package com.example.gamesaledb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gamesaledb.navigation.AppNavigation
import com.example.gamesaledb.ui.theme.GamesaleDBTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GamesaleDBTheme {
                AppNavigation()
            }
        }
    }
}