package com.example.habittrackerapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun EditHabit(navController: NavHostController){
    Text(text = "Edit Habit Screen")
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditHabitPreview(){
   EditHabit(rememberNavController())
}