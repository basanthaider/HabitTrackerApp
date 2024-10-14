package com.example.habittrackerapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.habittrackerapp.models.Habit
import com.example.habittrackerapp.repository.HabitViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabit(
    navController: NavHostController,
    habitViewModel: HabitViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    habitName: String // Pass the habit name as a parameter
) {
    var habit by remember { mutableStateOf<Habit?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var repeat by remember { mutableStateOf(listOf<String>()) }
    var reminderTime by remember { mutableStateOf<LocalTime?>(null) }
    var startFrom by remember { mutableStateOf(LocalDate.now()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(habitName) {
        habit = habitViewModel.getHabitByName(habitName)
        if (habit != null) {
            name = habit!!.name
            description = habit!!.description
            repeat = habit!!.repeat
            reminderTime = habit!!.reminder?.let { LocalTime.of(it.hour, it.minute, it.second, it.nano) }
            startFrom = if (habit!!.startFrom.isNotEmpty()) {
                LocalDate.parse(habit!!.startFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } else {
                LocalDate.now()
            }
        }
        isLoading = false
    }

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        habit?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Edit Habit",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = repeat.joinToString(", "),
                    onValueChange = {
                        repeat = it.split(",").map { day -> day.trim() }.filter { day -> day.isNotEmpty() }
                    },
                    label = { Text("Repeat (e.g., Everyday, Weekdays)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Set Reminder:")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = reminderTime != null,
                        onCheckedChange = { isChecked ->
                            reminderTime = if (isChecked) LocalTime.now() else null
                        }
                    )
                }
                if (reminderTime != null) {

                    OutlinedTextField(
                        value = reminderTime.toString(),
                        onValueChange = {
                            reminderTime = try {
                                LocalTime.parse(it)
                            } catch (e: Exception) {
                                reminderTime // Keep previous value on parse failure
                            }
                        },
                        label = { Text("Reminder Time (HH:MM)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = startFrom.toString(),
                    onValueChange = {
                        startFrom = try {
                            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        } catch (e: Exception) {
                            startFrom // Keep previous value on parse failure
                        }
                    },
                    label = { Text("Start From (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Save Changes Button
                Button(
                    onClick = {

                        habitViewModel.updateHabit(
                            originalName = habitName, // Use the original name to identify the habit
                            newName = name,
                            description = description,
                            repeat = repeat,
                            reminder = reminderTime,
                            startFrom = startFrom
                        )
                        navController.popBackStack() // Navigate back to Home
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }
            }
        } ?: run {
            // Handle case where habit is null (e.g., show an error message)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Habit not found.")
            }
        }
    }
}
