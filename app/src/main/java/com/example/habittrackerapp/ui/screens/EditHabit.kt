package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun EditHabit(navController: NavHostController) {
    Text(
        text = "Edit Habit Screen",
        modifier = Modifier
            .fillMaxSize()
            .size(32.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditHabitPreview() {
    EditHabit(rememberNavController())

}