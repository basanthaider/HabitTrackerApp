package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.utils.CalendarView

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Column {
            Text(
                text = "My Habits",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                CalendarView()
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = {},
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp),
                        containerColor = Color(0xff485d92),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}

