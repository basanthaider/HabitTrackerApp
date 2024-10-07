package com.example.habittrackerapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF4A6EA8) // Background color of the screen
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Forgot Password",
                style = TextStyle(fontSize = 35.sp, color = Color.White),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email address") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Button(
                onClick = { sendPasswordResetEmail(email, navController) },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(text = "Send Reset Email", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Go back to login",
                color = Color.White,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(top = 16.dp)
            )
        }
    }
}

private fun sendPasswordResetEmail(email: String, navController: NavHostController) {
    if (email.isNotEmpty()) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   // Toast.makeText(LocalContext.current, "Reset email sent!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Go back to login screen
                } else {
                    val errorMessage = task.exception?.message ?: "Error sending reset email"
                   // Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    } else {
        //Toast.makeText(LocalContext.current, "Please enter your email address", Toast.LENGTH_SHORT).show()
    }
}
