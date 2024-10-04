package com.example.habittrackerapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun AddHabit(navController: NavHostController){
    Text(text = "Add Habit Screen")
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddHabitPreview(){
    AddHabit(rememberNavController())
}