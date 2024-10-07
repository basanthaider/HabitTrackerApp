package com.example.habittrackerapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val calendarState = rememberUseCaseState()
    Scaffold { innerPadding ->
        Column {
            Text(
                text = "My Habits",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            CalendarDialog(
                state = calendarState,
                selection = CalendarSelection.Date { date ->
                    Log.d("trace", "Selected date: ${date.dayOfWeek}")
                },
                config = CalendarConfig(
                    style = CalendarStyle.WEEK,
                ),
            )
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    //Add Habit
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("/addHabit")
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp),
                        containerColor = Color(0xff485d92),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                    // Calendar
                    FloatingActionButton(
                        onClick = {
                            calendarState.show()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp),
                        containerColor = Color(0xff485d92),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Calendar")
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

