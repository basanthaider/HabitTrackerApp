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

    LaunchedEffect(Unit) {
        delay(3000)

        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (isLoggedIn) {

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                UserSession.userId = currentUser.uid
                navController.navigate("/home") {
                    popUpTo("/splash") { inclusive = true }
                }
            } else {
                navController.navigate("/login") {
                    popUpTo("/splash") { inclusive = true }
                }
            }
        } else {
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
            .background(color = Color(0xFFc5d6f4))
    ) {

        val imageScale = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            imageScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            )
        }


        val textOpacity = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            delay(500)
            textOpacity.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = "Welcome to Habity App!",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .alpha(textOpacity.value)
                    .padding(bottom = 16.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_lifestyle),
                contentDescription = "Splash Icon",
                modifier = Modifier
                    .size(300.dp)
                    .scale(imageScale.value)
            )
        }
        }
    }
