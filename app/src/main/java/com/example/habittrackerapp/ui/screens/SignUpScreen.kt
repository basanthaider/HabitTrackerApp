import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance() // Moved to local variable for better clarity

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF4A6EA8) // Background color of the screen
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 180.dp) // Spacing below the top image
            ) {
                // Register text with matching font style
                Text(
                    text = "Register",
                    style = TextStyle(
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(top = 5.dp)
                )

                // Create your account text
                Text(
                    text = "Create your account",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                    ),
                    modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
                )

                // Username TextField with Icon
                TextField(
                    value = username, // Use the username state variable
                    onValueChange = { username = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user), // Replace with a user icon
                            contentDescription = "User Icon",
                            tint = Color.Gray
                        )
                    },
                    placeholder = { Text(text = "Username") },
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

                // Email TextField with Icon
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email), // Replace with an email icon
                            contentDescription = "Email Icon",
                            tint = Color.Gray
                        )
                    },
                    placeholder = { Text(text = "Email address") },
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

                // Password TextField with Icon
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_password), // Replace with a password icon
                            contentDescription = "Password Icon",
                            tint = Color.Gray
                        )
                    },
                    placeholder = { Text(text = "Password") },
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

                // Confirm Password TextField with Icon
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_password), // Replace with a password icon
                            contentDescription = "Confirm Password Icon",
                            tint = Color.Gray
                        )
                    },
                    placeholder = { Text(text = "Confirm password") },
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

                // By registering text
                Text(
                    text = "By registering, you are agreeing to our\nTerms of use and Privacy Policy.",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    lineHeight = 14.sp
                )

                // Register Button
                Button(
                    onClick = {
                        if (password == confirmPassword) { // Check if passwords match
                            registerUser(email, password, navController)
                        } else {
                           // Toast.makeText(LocalContext.current, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0B2F60) // Dark blue
                    )
                ) {
                    Text(text = "REGISTER", color = Color.White)
                }

                // Already have an account? Login text with a clickable link
                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Already have an account?", color = Color.White)
                    Text(
                        text = " Login",
                        color = Color.Yellow,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                            .clickable {
                                navController.navigate("/login")
                            }
                    )
                }
            }
        }
    }
}

// Place the registerUser function outside the composable
private fun registerUser(email: String, password: String, navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Registration successful, navigate to login or home screen
                navController.navigate("/login")
            } else {
                // Handle the error (e.g., show a Snackbar with error message)
                val errorMessage = task.exception?.message ?: "Registration failed"
                Log.e("RegisterUser", errorMessage) // Log the error

            }
        }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(rememberNavController())
}
