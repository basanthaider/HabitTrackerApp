package com.example.habittrackerapp.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.ui.theme.Blue
import com.example.habittrackerapp.ui.theme.White
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // Function to handle user logout
    fun logoutUser() {
        // Sign out from Firebase
        auth.signOut()

        // Clear login state from SharedPreferences
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("is_logged_in", false)
            apply()
        }

        // Optionally, show a confirmation message
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate to LoginScreen and clear back stack
        navController.navigate("/login") {
            popUpTo("/splash") { inclusive = true } // Removes SplashScreen from back stack
            launchSingleTop = true
        }
    }
    // Existing SettingsScreen UI
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(top = 164.dp)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            SettingsCard(
                title = "Logout",
                description = "Come again quickly!",
                iconRes = Icons.Default.ExitToApp,
                onClick = {
                    logoutUser() // Call the logout function here
                }
            )
        }
        item {
            SettingsCard(
                title = "Rate us",
                description = "Your honest opinion helps us",
                iconRes = Icons.Default.ThumbUp,
                onClick = { /* Handle click */ }
            )
        }
        item {
            SettingsCard(
                title = "Send feedback",
                description = "Tell others how you enjoy our app",
                iconRes = Icons.Default.Star,
                onClick = { /* Handle click */ }
            )
        }
        item {
            SettingsCard(
                title = "Share app",
                description = "Share our app with friends and family",
                iconRes = Icons.Default.Share,
                onClick = { /* Handle click */ }
            )
        }
        item {
            SettingsCard(
                title = "Help",
                description = "FAQ, contact us & privacy policy",
                iconRes = Icons.Default.Info,
                onClick = { /* Handle click */ }
            )
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    description: String,
    iconRes: ImageVector, // Icon resource
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blue
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = iconRes,
                contentDescription = null,
                tint = White, // Customize the color if needed
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = White,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(rememberNavController())
}
