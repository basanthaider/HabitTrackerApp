package com.example.habittrackerapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.habittrackerapp.repository.HabitViewModel
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
fun HomeScreen(
    navController: NavHostController,
    habitViewModel: HabitViewModel = viewModel(),
    userId: String
) {
    val calendarState = rememberUseCaseState()

    // State for selected date
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // State for the user's habits
    var userHabits by remember { mutableStateOf(emptyList<String>()) } // Replace String with your Habit data model

    // Fetch habits when the selected date changes
    LaunchedEffect(selectedDate) {
        userHabits = habitViewModel.getHabitsForUserOnDate(userId, selectedDate)
    }


    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    text = "My Habits for $selectedDate",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    navController.navigate("/login" ){
                        popUpTo("/login") { inclusive = true }
                    }
                }) {
                    Text(text = "Logout")
                }
            }
            LazyColumn(
                modifier = Modifier.weight(0.5f)
            ) {
                items(userHabits) { habit ->
                    Log.d("trace", habit)
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Blue
                        ),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = habit, color = White, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(checked = false, onCheckedChange = {

                            })
                        }
                    }

                }

            }

            CalendarDialog(
                state = calendarState,
                selection = CalendarSelection.Date { date ->
                    Log.d("trace", "Selected date: ${date.dayOfWeek}")
                    selectedDate =
                        LocalDate.parse(date.toString()) // Correct conversion to LocalDate
                },
                config = CalendarConfig(
                    style = CalendarStyle.WEEK,
                ),
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .weight(0.05f)
                    .padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    //Add Habit
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("/addHabit")
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 8.dp),
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
                            .padding(start = 16.dp, bottom = 8.dp),
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
/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(userId) {
    HomeScreen(navController = rememberNavController(), userId = userId)
}*/
