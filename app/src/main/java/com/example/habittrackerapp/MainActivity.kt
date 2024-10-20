package com.example.habittrackerapp

import android.annotation.SuppressLint
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.habittrackerapp.repository.HabitViewModel
import com.example.habittrackerapp.ui.screens.NavHostScreen
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

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Create an instance of HabitViewModel
        val habitViewModel = HabitViewModel()

        // Create Notification Channel
        createNotificationChannel()

        // Initialize notification permission launcher
        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission granted; you can send a notification if needed
                // Hide the dialog when permission is granted
                showPermissionDeniedDialog.value = false
                // sendNotification(this)
            } else {
                // Check if the user has denied the permission before without selecting "Don't ask again"
                val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                if (!prefs.getBoolean(KEY_PERMISSION_DENIED_ONCE, false)) {
                    // Show dialog if permission denied for the first time
                    showPermissionDeniedDialog.value = true
                } else {
                    // Show a toast message if permission was denied previously
                    Toast.makeText(this, "You need to allow notifications to activate reminders!", Toast.LENGTH_LONG).show()
                }
            }
        }


        setContent {
            HabitTrackerAppTheme {
                // Main UI
                NavHostScreen(
                    habitViewModel = habitViewModel,
                    onRequestNotificationPermission = { checkAndRequestNotificationPermission() }
                )

                // Show the dialog when permission is denied for the first time
                if (showPermissionDeniedDialog.value) {
                    PermissionDeniedDialog(
                        onActionClicked = { allowPressed ->
                            if (allowPressed) {
                                // User pressed "Allow"
                                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                // Set the flag that permission was denied
                                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().putBoolean(KEY_PERMISSION_DENIED_ONCE, true).apply()
                                showPermissionDeniedDialog.value = false
                                // Show the toast message on denial
                                Toast.makeText(this, "You need to allow notifications to activate reminders!", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                }
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
            // sendNotification(this)
        } else {
            // If permission hasn't been requested yet, request it
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    @Composable
    private fun PermissionDeniedDialog(onActionClicked: (Boolean) -> Unit) {
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            confirmButton = {
                TextButton(onClick = { onActionClicked(true) }) {
                    Text(text = "Allow")
                }
            },
            dismissButton = {
                TextButton(onClick = { onActionClicked(false) }) {
                    Text(text = "Don't ask again")
                }
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.alert),
                    contentDescription = "Warning"
                )
            },
            title = {
                Text(text = "Permission Needed")
            },
            text = {
                Text(text = "Enable notifications to activate reminders and stay on track with your habits!")
            }
        )
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

