package com.example.habittrackerapp.ui.screens

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.habittrackerapp.utils.UserSession
import com.example.habittrackerapp.R
import com.example.habittrackerapp.ui.theme.Blue
import com.example.habittrackerapp.ui.theme.DarkBlue

@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Simulate a splash screen delay
    LaunchedEffect(Unit) {
        delay(3000) // 1 second delay for splash effect

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

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFc5d6f4)) // Light blue background
    ) {
        // Animate the scale of the image
        val imageScale = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            imageScale.animateTo(
                targetValue = 1f, // Animate to full scale
                animationSpec = tween(
                    durationMillis = 1000, // Animation duration
                    easing = LinearEasing // Adjust easing for desired effect
                )
            )
        }

        // Animate the text opacity
        val textOpacity = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            delay(500) // Delay before starting the animation
            textOpacity.animateTo(
                targetValue = 1f, // Animate to full opacity
                animationSpec = tween(
                    durationMillis = 500, // Animation duration
                    easing = LinearOutSlowInEasing // Ease in for smooth appearance
                )
            )
        }

        // Place the text above the image
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Text at the top center
            Text(
                text = "Welcome to Habity App!",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .alpha(textOpacity.value) // Apply the animated opacity
                    .padding(bottom = 16.dp) // Add padding between text and image
            )

            // Image below the text
            Image(
                painter = painterResource(id = R.drawable.ic_lifestyle),
                contentDescription = "Splash Icon",
                modifier = Modifier
                    .size(300.dp) // Set the size of the icon
                    .scale(imageScale.value) // Apply the animated scale
            )
        }
        }
    }
