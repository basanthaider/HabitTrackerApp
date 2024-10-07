import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.R
import androidx.compose.runtime.setValue

import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.*





@Composable
fun LoginScreen(navController: NavHostController) {

    var rememberMeChecked by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF4A6EA8) // Background color of the screen
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            // Curved background shape
            CurvedBackground(modifier = Modifier
                .fillMaxWidth()
                .height(350.dp))

            // Image that fills the entire white area
            Image(
                painter = painterResource(id = R.drawable.habit2), // Replace with your image
                contentDescription = "App Illustration",
                modifier = Modifier
                    .fillMaxWidth() // Ensure the image takes full width
                    .height(350.dp) // Match the height of the curved background
                    .clip(CurvedImageShape()) // Clip the image to the curved shape
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillBounds // Scale the image to fill the entire bounds
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 300.dp) // Adjust this for proper spacing below the curved image
            ) {
                // Welcome Back Text
                Text(
                    text = "Welcome Back",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,

                        ),
                    modifier = Modifier.padding(top = 25.dp)
                )

                // Login to your account
                Text(
                    text = "Login to your account",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
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
                            painter = painterResource(id = R.drawable.ic_password), // replace with a password icon
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

                // Remember me & Forgot Password Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row {
                        Checkbox(
                            checked = rememberMeChecked,
                            onCheckedChange = { rememberMeChecked = it }
                        )
                        Text(
                            text = "Remember me",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Forgot Password?",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterVertically)
                            .clickable {
                                navController.navigate("/forget")

                            }
                    )
                }

                // Login Button
                Button(
                    onClick = {
                        loginUser(email, password, navController)
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
                    Text(text = "LOGIN", color = Color.White)
                }

                // Sign Up text with a clickable link
                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Don’t have an account?", color = Color.White)
                    Text(
                        text = " Sign up",
                        color = Color.Yellow,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                            .clickable {
                                navController.navigate("/register")
                                // Handle the click event here

                            }
                    )
                }   // Add the rest of your form fields (username, password, login button, etc.) below
            }
        }
    }
}

// Function to handle login
private fun loginUser(email: String, password: String, navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Login successful, navigate to home screen
                navController.navigate("/home")
            } else {
                // Handle the error (e.g., show a Snackbar with an error message)
                val errorMessage = task.exception?.message ?: "Login failed"
                Log.e("LoginUser", errorMessage) // Log the error
                // Optionally, show an error message to the user

            }
        }
}


@Composable
fun CurvedBackground(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.8f) // Start point for the curve
            cubicTo(
                size.width * 0.5f, size.height * 1.2f, // Control point 1
                size.width * 0.5f, size.height * 0.4f, // Control point 2
                size.width, size.height * 0.8f  // End point for the curve
            )
            lineTo(size.width, 0f) // Draw the top line to the right edge
            lineTo(0f, 0f) // Draw the top line back to the left edge
            close() // Close the path
        }

        drawPath(path = path, color = Color.White) // Use white color for the curved background
    }
}

// Custom shape for clipping the image to fit into the curve
fun CurvedImageShape() = GenericShape { size, _ ->
    moveTo(0f, size.height * 0.8f)
    cubicTo(
        size.width * 0.5f, size.height * 1.2f,  // Control point 1
        size.width * 0.5f, size.height * 0.4f,  // Control point 2
        size.width, size.height * 0.8f           // End of the curve
    )
    lineTo(size.width, 0f) // Line to the right top
    lineTo(0f, 0f) // Line back to the left top
    close() // Close the path
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}
