package com.example.habittrackerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.example.habittrackerapp.repository.HabitViewModel
import com.example.habittrackerapp.ui.screens.NavHostScreen
import com.example.habittrackerapp.ui.screens.PermissionDeniedDialog
import com.example.habittrackerapp.ui.theme.HabitTrackerAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>
    private val PREFS_NAME = "app_prefs"
    private val KEY_PERMISSION_DENIED_ONCE = "permission_denied_once"
    private var showPermissionDeniedDialog = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)

        val habitViewModel = HabitViewModel()

        createNotificationChannel()

        initializeNotificationPermissionLauncher()

        setContent {
            HabitTrackerAppTheme {
                // Main UI
                NavHostScreen(
                    habitViewModel = habitViewModel,
                    onRequestNotificationPermission = {
                        checkAndRequestNotificationPermission()
                    }
                )

                // Show the dialog when permission is denied for the first time
                handlePermissionDenied()
            }
        }
    }

    // This function checks if the permission is already granted
    private fun checkAndRequestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, you can send a notification if needed
        } else {
            // If permission hasn't been requested yet, request it
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun initializeNotificationPermissionLauncher() {
        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission granted; you can send a notification if needed
                // Hide the dialog when permission is granted
                showPermissionDeniedDialog.value = false
            } else {
                // Check if the user has denied the permission before without selecting "Don't ask again"
                val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                if (!prefs.getBoolean(KEY_PERMISSION_DENIED_ONCE, false)) {
                    // Show dialog if permission denied for the first time
                    showPermissionDeniedDialog.value = true
                } else {
                    // Show a toast message if permission was denied previously
                    Toast.makeText(
                        this,
                        "You need to allow notifications to activate reminders!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    @Composable
    private fun handlePermissionDenied() {
        if (showPermissionDeniedDialog.value) {
            PermissionDeniedDialog(
                onActionClicked = { allowPressed ->
                    if (allowPressed) {
                        // User pressed "Allow"
                        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        // Set the flag that permission was denied
                        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                            .putBoolean(KEY_PERMISSION_DENIED_ONCE, true).apply()
                        showPermissionDeniedDialog.value = false
                        // Show the toast message on denial
                        Toast.makeText(
                            this,
                            "You need to allow notifications to activate reminders!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }

    }


    private fun createNotificationChannel() {

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1", "Reminder Channel", importance).apply {
            description = "Shows reminder notifications."
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

