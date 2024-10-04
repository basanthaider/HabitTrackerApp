package com.example.habittrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.habittrackerapp.ui.screens.MainScreen
import com.example.habittrackerapp.ui.theme.HabitTrackerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerAppTheme {
                MainScreen()
            }
        }
    }
}
