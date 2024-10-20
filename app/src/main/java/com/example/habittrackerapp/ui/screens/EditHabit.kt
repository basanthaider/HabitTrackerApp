package com.example.habittrackerapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.habittrackerapp.models.Habit
import com.example.habittrackerapp.repository.HabitViewModel
import com.example.habittrackerapp.ui.theme.Blue
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.models.DisplayMode
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionSelection
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabit(
    navController: NavHostController,
    habitViewModel: HabitViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    habitName: String // Name of the habit to be edited
) {
    var habit by remember { mutableStateOf<Habit?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var repeat by remember { mutableStateOf(listOf<String>()) }
    var reminderTime by remember { mutableStateOf<LocalTime?>(null) }
    var startFrom by remember { mutableStateOf(LocalDate.now()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    val optionState = rememberUseCaseState()
    val calendarState = rememberUseCaseState()
    val timeState = rememberUseCaseState()
    val context = LocalContext.current

    LaunchedEffect(habitName) {
        try {
            habit = habitViewModel.getHabitByName(habitName)
            habit?.let {

                name = it.name
                description = it.description
                repeat = it.repeat
                reminderTime =
                    it.reminder?.let { LocalTime.of(it.hour, it.minute, it.second, it.nano) }
                startFrom = if (it.startFrom.isNotEmpty()) {
                    LocalDate.parse(
                        it.startFrom,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    ) // Parse the start date
                } else {
                    LocalDate.now()
                }
            } ?: run {

                errorMessage = "Habit not found."
            }
        } catch (e: Exception) {
            // Log any errors encountered during data fetching
            Log.e("EditHabit", "Error fetching habit: ${e.message}")
            errorMessage = "Error fetching habit."
        } finally {

            isLoading = false
        }
    }


    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading habit...")
            }
        }
    } else {

        errorMessage.takeIf { it.isNotBlank() }?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        } ?: run {

            habit?.let {

                Box(modifier = Modifier.fillMaxSize()) {

                    Text(
                        text = "Edit your habit",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(top = 56.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {


                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Habit Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text(" Habit Description") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Button(
                                onClick = { optionState.show() },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = ButtonDefaults.buttonColors(Blue),
                                shape = RoundedCornerShape(24)
                            ) {
                                Text(text = "Select Repeat Days")
                            }

                            Spacer(modifier = Modifier.weight(1f))


                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(16)
                            ) {
                                Text(
                                    text = repeat.joinToString(", "),
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 15.sp // Font size for display
                                )
                            }
                        }

                        OptionDialog(
                            state = optionState,
                            selection = OptionSelection.Multiple(
                                options = listOf(
                                    Option(titleText = "Saturday"),
                                    Option(titleText = "Sunday"),
                                    Option(titleText = "Monday"),
                                    Option(titleText = "Tuesday"),
                                    Option(titleText = "Wednesday"),
                                    Option(titleText = "Thursday"),
                                    Option(titleText = "Friday"),
                                    Option(titleText = "Everyday"),
                                ),
                                onSelectOptions = { _, selectedOptions ->
                                    repeat = selectedOptions.map { it.titleText }
                                }
                            ),
                            config = OptionConfig(mode = DisplayMode.GRID_VERTICAL)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Button(
                                onClick = { calendarState.show() },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = ButtonDefaults.buttonColors(Blue),
                                shape = RoundedCornerShape(24)
                            ) {
                                Text(text = "Start From")
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Surface(
                                modifier = Modifier.padding(top = 16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(16)
                            ) {
                                Text(
                                    text = "Start From: ${startFrom.dayOfMonth}/${startFrom.monthValue}",
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 15.sp
                                )
                            }
                        }


                        CalendarDialog(
                            state = calendarState,
                            selection = CalendarSelection.Date { date ->
                                startFrom = date
                            },
                            config = CalendarConfig()
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Button(
                                onClick = { timeState.show() },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = ButtonDefaults.buttonColors(Blue),
                                shape = RoundedCornerShape(24)
                            ) {
                                Text(text = "Set Reminder") // Button text
                            }

                            Spacer(modifier = Modifier.weight(1f))


                            Surface(
                                modifier = Modifier.padding(top = 16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(16)
                            ) {
                                Text(
                                    text = "Reminder: ${reminderTime?.hour}:${reminderTime?.minute ?: "Not Set"}",
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 15.sp
                                )
                            }
                        }


                        ClockDialog(
                            state = timeState,
                            selection = ClockSelection.HoursMinutes { hours, minutes ->
                                reminderTime = LocalTime.of(
                                    hours,
                                    minutes
                                )
                            },
                            config = ClockConfig()
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        val isInputValid =
                            name.isNotBlank() && description.isNotBlank()
                        Button(
                            onClick = {

                                habitViewModel.updateHabit(
                                    originalName = habitName,
                                    newName = name,
                                    description = description,
                                    repeat = repeat,
                                    reminder = reminderTime,
                                    startFrom = startFrom,
                                    context = context
                                )
                                navController.popBackStack()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .padding(top = 16.dp),
                            enabled = isInputValid,
                            colors = ButtonDefaults.buttonColors(Blue),
                            shape = RoundedCornerShape(24)
                        ) {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }
}