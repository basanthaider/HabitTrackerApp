package com.example.habittrackerapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.repository.HabitViewModel
import com.example.habittrackerapp.ui.theme.Blue
import com.example.habittrackerapp.ui.theme.DarkBlue
import com.example.habittrackerapp.ui.theme.White
import com.example.habittrackerapp.utils.UserSession
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
) {
    val calendarState = rememberUseCaseState()

    val userId = UserSession.userId
    val context = LocalContext.current
    // State for selected date
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    // State for the user's habits
    var userHabits by remember { mutableStateOf<List<Map<String, Boolean>>>(emptyList()) }
    // Fetch habits when the selected date changes
    LaunchedEffect(selectedDate) {
        userHabits = habitViewModel.storeHabitsInMap(userId, selectedDate)
    }


    Surface {
        Log.d("trace", "userHabitss: $userHabits")
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Blue
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's habits,$selectedDate",
                            fontSize = 18.sp,
                            color = White,
                            modifier = Modifier.padding(8.dp),
                        )
                        IconButton(onClick = { calendarState.show() }
                        ) {
                            Icon(
                                Icons.Filled.DateRange,
                                contentDescription = "Calendar",
                                tint = White
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.weight(0.5f)
            ) {
                items(userHabits) { habit ->
                    var isDone by remember { mutableStateOf(habit.values.first()) }
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkBlue
                        ),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.habit),
                                contentDescription = "streak",
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(8.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 16.dp)
                            ) {
                                Text(
                                    text = habit.keys.first(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                                // Optional: Add a subtitle or description here
                            }
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Checkbox(
                                    checked = isDone,
                                    onCheckedChange = {
                                        isDone = !isDone
                                        val updatedHabitss = userHabits.map {
                                            if (it.keys.first() == habit.keys.first()) {
                                                mapOf(it.keys.first() to isDone) // Update the map with the new value
                                            } else {
                                                it}
                                        }
                                        userHabits = updatedHabitss
                                        habitViewModel.doneHabit(
                                            userId,
                                            habit.keys.first(),
                                            context,
                                            isDone
                                        )
                                    },
                                    modifier = Modifier.padding(end = 8.dp),
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = DarkBlue,
                                        uncheckedColor = White,
                                        checkmarkColor = White,
                                    )
                                )
                                IconButton(
                                    onClick = { navController.navigate("/editHabit") },
                                    modifier = Modifier.wrapContentWidth(align = Alignment.End)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_edit),
                                        contentDescription = "Edit",
                                        tint = Color.Unspecified
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        habitViewModel.deleteHabit(
                                            userId,
                                            habit.keys.first(),
                                            navController.context
                                        )
                                        navController.navigate("/home") {
                                            popUpTo("/home") { inclusive = true }
                                        }
                                    },
                                    modifier = Modifier.wrapContentWidth(align = Alignment.End)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "Delete",
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Box(modifier = Modifier.padding(8.dp))
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

        }

    }
}
/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(userId) {
    HomeScreen(navController = rememberNavController(), userId = userId)
}*/
