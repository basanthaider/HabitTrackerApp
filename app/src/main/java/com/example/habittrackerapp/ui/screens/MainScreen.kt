package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.habittrackerapp.utils.NavItem

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Profile", Icons.Default.Person),
    )
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label) })
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int = 0) {
    when (selectedIndex) {
        0 -> HomeScreen()
        1 -> ProfileScreen()

    }
}