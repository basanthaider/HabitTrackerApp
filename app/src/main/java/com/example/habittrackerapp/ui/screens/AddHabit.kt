package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun AddHabit(navController: NavHostController){
    Text(
        text = "Add a New Habit",
        fontSize = 24.sp,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}