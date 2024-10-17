package com.example.habittrackerapp

import RequestNotificationPermission
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.getSystemService
import com.example.habittrackerapp.repository.HabitViewModel
import com.example.habittrackerapp.ui.screens.NavHostScreen
import com.example.habittrackerapp.ui.theme.HabitTrackerAppTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //intialize firebase
        FirebaseApp.initializeApp(this)
        // Create Notification Channel
        createNotificationChannel()
        // Create an instance of HabitViewModel
        val habitViewModel = HabitViewModel()
        // Get the current user from FirebaseAuth


        setContent {
            HabitTrackerAppTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Call the permission request function here
                    RequestNotificationPermission()

                    // Pass habitRepository and userId to NavHostScreen
                    NavHostScreen(habitViewModel = habitViewModel)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Habit Tracker Notifications"
            val descriptionText = "Channel for Habit Tracker notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }
}

