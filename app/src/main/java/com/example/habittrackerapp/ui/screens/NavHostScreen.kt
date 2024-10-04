package com.example.habittrackerapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostScreen(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination ="/home" ) {


        composable(route="/home"){
            HomeScreen(navController)
        }
        composable(route="/addHabit") {
            AddHabit(navController)
        }

    }
}