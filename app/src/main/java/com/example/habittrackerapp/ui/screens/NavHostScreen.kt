package com.example.habittrackerapp.ui.screens

import LoginScreen
import RegisterScreen
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.habittrackerapp.R
import com.example.habittrackerapp.repository.HabitViewModel
import com.example.habittrackerapp.ui.theme.Blue
import com.example.habittrackerapp.ui.theme.DarkBlue
import com.example.habittrackerapp.ui.theme.White
import com.example.habittrackerapp.utils.NavItem
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavHostScreen(habitViewModel: HabitViewModel) {

    val auth = FirebaseAuth.getInstance()
    var currentUser by remember { mutableStateOf(auth.currentUser) }
    val navController = rememberNavController()
    var bottomBarVisibility by remember {
        mutableStateOf(true)
    }
    var topAppBarVisibility by remember {
        mutableStateOf(currentUser != null)
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(visible = topAppBarVisibility) {
                TopAppBar(navController = navController)
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = bottomBarVisibility) {
                NavigationBottomBar(
                    navController = navController,
                    items = listOf(
                        NavItem(label = "/home", icon = Icons.Default.Home),
                        NavItem(label = "/profile", icon = Icons.Default.Person)
                    )
                )

            }


        }) {
        NavHost(
            //لما نخلص خالص هنحط السطرين دول متمسحهومش
            // navController = navController,
            // startDestination = if (currentUser != null) "home" else "login"
            navController = navController, startDestination = "/login",
            modifier = Modifier.padding(it)
        ) {
            composable(route = "/login") {
                bottomBarVisibility = false
                topAppBarVisibility = false

                LoginScreen(navController)
            }
            composable(route = "/forget") {
                bottomBarVisibility = false
                topAppBarVisibility = false

                ForgotPasswordScreen(navController)
            }
            composable(route = "/register") {
                bottomBarVisibility = false
                topAppBarVisibility = false

                RegisterScreen(navController)
            }

            composable(
                route = "/home/{userId}",
                arguments = listOf(
                    navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                bottomBarVisibility = true
                topAppBarVisibility = true // Hide top app bar on login screen
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                HomeScreen(navController, habitViewModel = HabitViewModel(), userId = userId)
            }
            composable(route = "/addHabit") {
                bottomBarVisibility = false
                AddHabit(
                    navController = navController,
                    habitViewModel = habitViewModel,
                    userId = "userId"
                )
            }
            composable(route = "/editHabit") {
                bottomBarVisibility = true
                topAppBarVisibility = true

                EditHabit(navController)
            }
            composable(route = "/settingsScreen") {
                bottomBarVisibility = true
                topAppBarVisibility = true

                SettingsScreen(navController)
            }
            composable(route = "/profile") {
                bottomBarVisibility = true
                topAppBarVisibility = true

                ProfileScreen(navController)
            }

        }
    }


}

@Composable
fun NavigationBottomBar(navController: NavHostController, items: List<NavItem>) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val context = LocalContext.current.applicationContext
    val currentRoute = navBackStackEntry.value?.destination?.route
    BottomAppBar(
        containerColor = Blue
    ) {
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
                    selectedIconColor = Blue,
                    selectedTextColor = White,
                    unselectedTextColor = DarkBlue,
                    unselectedIconColor = White

                )


            )

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavHostController) {
    androidx.compose.material3.TopAppBar(
        title = { Text(text = "Habity") },
        navigationIcon = {
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lifestyle),
                    contentDescription = "Habity Icon"
                )

            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue,
            titleContentColor = White,
            navigationIconContentColor = White
        ), actions = {
            IconButton(
                onClick = { /*TODO*/ },
            )
            {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notification Icon",
                    tint = White
                )
            }
            IconButton(
                onClick = { navController.navigate("/settingsScreen") },
            )
            {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = " Settings Icon",
                    tint = White
                )
            }

        }
    )
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun NavHostScreenPreview() {
//    // Pass dummy values for preview purposes
//    NavHostScreen(habitViewModel = HabitViewModel(), userId = "dummyUserId")
//}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TopAppBarPreview() {
    TopAppBar(rememberNavController())
}