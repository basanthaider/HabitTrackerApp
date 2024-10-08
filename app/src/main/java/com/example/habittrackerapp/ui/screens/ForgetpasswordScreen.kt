package com.example.habittrackerapp.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.habittrackerapp.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val context= LocalContext.current

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
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email), // replace with a user icon
                        contentDescription = "User Icon",
                        tint = Color.Gray
                    )
                },
                placeholder = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email, // Use email keyboard type
                    imeAction = ImeAction.Next // Next action when done typing
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(56.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )



            Button(
                onClick = { sendPasswordResetEmail(email, navController,context) },
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

private fun sendPasswordResetEmail(email: String, navController: NavHostController,context: Context) {
    if (email.isNotEmpty()) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   Toast.makeText(context, "Reset email sent!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Go back to login screen
                } else {
                    val errorMessage = task.exception?.message ?: "Error sending reset email"
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    } else {
        Toast.makeText(context, "Please enter your email address", Toast.LENGTH_SHORT).show()
    }
}
