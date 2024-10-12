package com.example.habittrackerapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import com.example.habittrackerapp.utils.UserSession
import com.example.habittrackerapp.R

@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Simulate a splash screen delay
    LaunchedEffect(Unit) {
        delay(1000) // 1 second delay for splash effect

        // Retrieve login state from SharedPreferences
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            // If the user chose to be remembered and is logged in, navigate to HomeScreen
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                UserSession.userId = currentUser.uid
                navController.navigate("/home") {
                    popUpTo("/splash") { inclusive = true }
                }
            } else {
                // If FirebaseAuth has no current user, navigate to LoginScreen
                navController.navigate("/login") {
                    popUpTo("/splash") { inclusive = true }
                }
            }
        } else {
            // If the user did not choose to be remembered, ensure they are logged out
            FirebaseAuth.getInstance().signOut()

            // Navigate to LoginScreen
            navController.navigate("/login") {
                popUpTo("/splash") { inclusive = true }
            }
        }
    }

    // UI for Splash Screen
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Replace Text with Image
        Image(
            painter = painterResource(id = R.drawable.ic_notification), // Replace with your icon resource
            contentDescription = "Splash Icon", // Content description for accessibility
            modifier = Modifier.size(300.dp) // Set the size of the icon
        )
    }
}
