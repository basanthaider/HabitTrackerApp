package com.example.habittrackerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.habittrackerapp.repository.HabitViewModel
import com.example.habittrackerapp.ui.screens.NavHostScreen
import com.example.habittrackerapp.ui.theme.HabitTrackerAppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create an instance of HabitViewModel
        val habitViewModel = HabitViewModel()

        // Get the current user from FirebaseAuth


        setContent {
            HabitTrackerAppTheme {
                // Pass habitRepository and userId to NavHostScreen
                NavHostScreen(habitViewModel=habitViewModel)
            }
        }
    }
}
