package com.example.habittrackerapp.ui.screens

import LoginScreen
import RegisterScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.utils.NavItem

@Composable
fun NavHostScreen(){
    val navController = rememberNavController()
    var bottomBarVisibility by remember{
        mutableStateOf(true)
    }
    Scaffold (bottomBar = {
        AnimatedVisibility(visible= bottomBarVisibility ) {
            NavigationBottomBar(
                navController = navController,
                items = listOf(
                    NavItem(label = "/home", icon = Icons.Default.Home),
                    NavItem(label = "/profile", icon = Icons.Default.Person)
                )
            )
            
        }


    }){
        NavHost(navController = navController, startDestination ="/home",
            modifier = Modifier.padding(it)) {
            composable(route="/login") {
                bottomBarVisibility= false
                LoginScreen(navController)
            }
            composable(route="/register") {
                bottomBarVisibility= false
                RegisterScreen(navController)
            }

            composable(route="/home"){
                bottomBarVisibility= true
                HomeScreen(navController)
            }
            composable(route="/addHabit") {
                bottomBarVisibility=false
                AddHabit(navController)
            }
            composable(route="/editHabit"){
                bottomBarVisibility =false
                EditHabit(navController)
            }
            composable(route="/profile") {
                bottomBarVisibility= true
                ProfileScreen(navController)
            }

        }
    }







}
@Composable
fun NavigationBottomBar(navController: NavHostController,items:List<NavItem>) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val context = LocalContext.current.applicationContext
    val currentRoute = navBackStackEntry.value?.destination?.route
    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.label,
                onClick = {
                    navController.navigate(item.label) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = androidx.compose.ui.graphics.Color.Blue,
                    selectedTextColor = androidx.compose.ui.graphics.Color.Blue,
                    unselectedTextColor = androidx.compose.ui.graphics.Color.Gray,
                    unselectedIconColor = androidx.compose.ui.graphics.Color.Gray
                )


            )

        }

    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NavHostScreenPreview() {
    NavHostScreen()
}