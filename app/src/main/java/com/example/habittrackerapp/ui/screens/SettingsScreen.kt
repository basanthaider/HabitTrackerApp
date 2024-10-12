package com.example.habittrackerapp.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.ui.theme.Blue
import com.example.habittrackerapp.ui.theme.White

@Composable
fun SettingsScreen(navController: NavHostController) {
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
                    navController.navigate("/login" ){
                        popUpTo("/login") { inclusive = true} }
                }
            )
        }
        item {
            SettingsCard(
                title = "Rate us",
                description = "you honest opinion help us",
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
                description = "Share our app with friend and family",
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