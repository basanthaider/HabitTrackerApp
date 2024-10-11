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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.habittrackerapp.repository.HabitRepository
import com.example.habittrackerapp.ui.theme.Blue
import com.example.habittrackerapp.ui.theme.White
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, habitRepository: HabitRepository, userId: String) {
    val calendarState = rememberUseCaseState()

    // State for selected date
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // State for the user's habits
    var userHabits by remember { mutableStateOf(emptyList<String>()) } // Replace String with your Habit data model

    // Fetch habits when the selected date changes
    LaunchedEffect(selectedDate) {
        userHabits = habitRepository.getHabitsForUserOnDate(userId, selectedDate)
    }
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "My Habits for $selectedDate",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )

            // Show the user's habits for the selected date
            userHabits.forEach { habit ->
                Text(text = habit, modifier = Modifier.padding(16.dp)) // Customize display as needed
            }

            CalendarDialog(
                state = calendarState,
                selection = CalendarSelection.Date { date ->
                    Log.d("trace", "Selected date: ${date.dayOfWeek}")
                    selectedDate = LocalDate.parse(date.toString()) // Correct conversion to LocalDate
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
                            .padding(end = 16.dp)
                            .padding(bottom = 8.dp),
                        containerColor = Blue,
                        contentColor = White
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
                            .padding(start = 16.dp)
                            .padding(bottom = 8.dp),
                        containerColor = Blue,
                        contentColor = White
                    ) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Calendar")
                    }
                }
            }
        }

    }
}
