package com.example.habittrackerapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.models.Habit
import com.example.habittrackerapp.repository.HabitRepository
import com.example.habittrackerapp.ui.theme.Blue
import com.example.habittrackerapp.ui.theme.White
import com.example.habittrackerapp.viewmodel.SharedViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController,sharedViewModel: SharedViewModel = viewModel() , userId: String) {
    val calendarState = rememberUseCaseState()
    val context = LocalContext.current
/*
    val habits = remember { mutableStateListOf<Habit>() } // Stores retrieved habits
*/

  /*  LaunchedEffect(Unit) {
        habitRepository.getHabits("hka3A2FLm3YnjP0w3CPG1LWa7Ek2")
    }*/
/*
    val getHabits = sharedViewModel.state.value
*/
    val habits by sharedViewModel.habitList.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Column {
            Text(
                text = "My Habits",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            LazyColumn (
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ){
                items(habits){
                    habit->
                    Row (modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text(text = habit.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(text = habit.description, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }

            }

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
/*
            // Display Habits in a LazyColumn
            if (habits.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(habits) { habit ->
                        HabitItem(habit) // Assuming you have a HabitItem composable
                    }
                }
            } else {
                Text(
                    text = "No habits found yet!",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }*/
        }
    }
}


@Composable
fun HabitItem(habit: Habit) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = habit.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = habit.description, style = MaterialTheme.typography.headlineSmall)
            // Add more UI elements as needed (e.g., repeat days, reminder time)
        }
    }
}

/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    val habitRepository = HabitRepository()
    val userId = "hka3A2FLm3YnjP0w3CPG1LWa7Ek2"

    HomeScreen(navController, habitRepository, userId)
}*/

