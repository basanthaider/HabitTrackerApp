package com.example.habittrackerapp

import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.ui.screens.HomeScreen
import com.example.habittrackerapp.ui.screens.NavHostScreen
import com.example.habittrackerapp.ui.screens.NavigationBottomBar
import com.example.habittrackerapp.ui.theme.HabitTrackerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerAppTheme {
               NavHostScreen()

            }
        }
    }
}
