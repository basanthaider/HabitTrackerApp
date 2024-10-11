package com.example.habittrackerapp.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.habittrackerapp.repository.HabitViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabit(navController: NavHostController, habitViewModel: HabitViewModel, userId: String) {
    var habitName by remember { mutableStateOf("") }
    var isHabitNameValid by remember { mutableStateOf(true) }
    var habitDescription by remember { mutableStateOf("") }
    var isHabitDescriptionValid by remember { mutableStateOf(true) }
    val calendarState = rememberUseCaseState()
    val optionState = rememberUseCaseState()
    val timeState = rememberUseCaseState()
    var selectedDays by remember { mutableStateOf(listOf<String>("Everyday")) }
    var reminderHours by remember { mutableIntStateOf(0) }
    val reminderMin by remember { mutableIntStateOf(0) }
    var reminder by remember { mutableStateOf(LocalTime.now()) }
    var isReminder by remember { mutableStateOf(false) }
    var startFrom by remember { mutableStateOf(LocalDate.now()) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
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
                        selectedDays = selectedOptions.map { it.titleText }
                        Log.d("trace", "Selected days: $selectedDays")
                    }
                ),
                config = OptionConfig(mode = DisplayMode.GRID_VERTICAL)
            )
            CalendarDialog(
                state = calendarState,
                selection = CalendarSelection.Date { date ->
                    startFrom = date
                    Log.d("trace", "Selected date: $startFrom")
                },
                config = CalendarConfig(
                    style = CalendarStyle.MONTH,
                ),
            )
            ClockDialog(
                state = timeState,
                selection = ClockSelection.HoursMinutes { hours, minutes ->
                    reminder = LocalTime.of(hours, minutes)
                    Log.d("trace", "Selected time: $reminder")
                },
                config = ClockConfig(
                    defaultTime = reminder,
                ))
            Text(
                text = "Add a New Habit",
                fontSize = 24.sp,
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                value = habitName,
                onValueChange = {
                    habitName = it
                    isHabitNameValid = it.isNotBlank()
                },
                isError = !isHabitNameValid,
                placeholder = {
                    Text(text = "Habit Name")
                },
                shape = RoundedCornerShape(16.dp),
            )
            if (!isHabitNameValid) {
                Text(
                    text = "Please enter Habit Name",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                value = habitDescription,
                onValueChange = {
                    habitDescription = it
                    isHabitDescriptionValid = it.isNotBlank()
                },
                isError = !isHabitDescriptionValid,
                placeholder = {
                    Text(text = "Habit Description")
                },
                shape = RoundedCornerShape(16.dp),
            )
            if (!isHabitDescriptionValid) {
                Text(
                    text = "Please enter Habit Description", color = Color.Red, fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row {
                Button(
                    onClick = {
                        optionState.show()
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(120.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(24),
                ) {
                    Text(text = "Repeat")
                }
                Spacer(modifier = Modifier.weight(1f))
                if (selectedDays.size >= 7 || selectedDays.contains("Everyday")) {
                    selectedDays = listOf("Everyday")
                }
                Surface(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    color = Color(0xffe1e2ec),
                    shape = RoundedCornerShape(16)
                ) {
                    Text(
                        text = selectedDays.joinToString(", "),
                        modifier = Modifier.padding(16.dp),
                        fontSize = 15.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Set Reminder",
                    modifier = Modifier.clickable {
                        timeState.show()
                        isReminder = true
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = isReminder, onCheckedChange = {
                    if (!isReminder)
                        timeState.show()

                    if (isReminder)
                        reminderHours = 0

                    isReminder = !isReminder
                })
            }
            if (reminderHours != 0 && isReminder) {
                val reminderMinStr: String =
                    (if (reminderMin == 0) {
                        "00"
                    } else {
                        reminderMin.toString()
                    }).toString()
                var amOrPm by remember { mutableStateOf("AM") }
                if (reminderHours > 12) {
                    reminderHours -= 12
                    amOrPm = "PM"
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = Color(0xffe1e2ec),
                        shape = RoundedCornerShape(16)
                    ) {
                        Text(
                            text = "Remind me at $reminderHours:$reminderMinStr $amOrPm",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            Row {
                Button(
                    onClick = {
                        calendarState.show()
                        Log.d("trace", "Selected date: $startFrom")
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(120.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(24),
                ) {
                    Text(text = "Start From")
                }
                Spacer(modifier = Modifier.weight(1f))
                val startFromStr: String = (
                        if (startFrom == LocalDate.now()) {
                            "Today"
                        } else {
                            "${startFrom.dayOfMonth}/${startFrom.monthValue}"
                        }
                        )
                Surface(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    color = Color(0xffe1e2ec),
                    shape = RoundedCornerShape(16)
                ) {
                    Text(
                        text = "Start From $startFromStr",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 15.sp
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (habitName.isNotBlank() && habitDescription.isNotBlank()) {
                        habitViewModel.addHabit(
                            userId = userId,
                            name = habitName,
                            description = habitDescription,
                            repeat = selectedDays,
                            reminder = reminder,
                            startFrom = startFrom,
                            context = context,
                            navController = navController
                        )
                    } else {
                        if (habitName.isBlank()) isHabitNameValid = false
                        if (habitDescription.isBlank()) isHabitDescriptionValid = false
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(24)
            ) {
                Text(text = "Save")
            }
        }
    }
}

