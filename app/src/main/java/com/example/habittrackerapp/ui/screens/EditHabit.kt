package com.example.habittrackerapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.TopAppBar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.habittrackerapp.models.Habit
import com.example.habittrackerapp.repository.HabitViewModel
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
    navController: NavHostController, // Navigation controller to handle screen transitions
    habitViewModel: HabitViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), // ViewModel to manage habit data
    habitName: String // Name of the habit to be edited
) {
    var habit by remember { mutableStateOf<Habit?>(null) } // Holds the current habit being edited
    var name by remember { mutableStateOf("") } // State variable for habit name input
    var description by remember { mutableStateOf("") } // State variable for habit description input
    var repeat by remember { mutableStateOf(listOf<String>()) } // State variable for selected repeat days
    var reminderTime by remember { mutableStateOf<LocalTime?>(null) } // State variable for reminder time
    var startFrom by remember { mutableStateOf(LocalDate.now()) } // State variable for start date
    var isLoading by remember { mutableStateOf(true) } // Loading state to manage loading UI
    var errorMessage by remember { mutableStateOf("") } // State variable for error messages
    val optionState = rememberUseCaseState() // State for the repeat days selection dialog
    val calendarState = rememberUseCaseState() // State for the calendar date selection dialog
    val timeState = rememberUseCaseState() // State for the time selection dialog
    val context = LocalContext.current // Get current context

    // Load habit data when the screen is displayed
    LaunchedEffect(habitName) {
        try {
            habit = habitViewModel.getHabitByName(habitName) // Fetch habit using the habit name
            habit?.let {
                // If the habit is found, populate the fields with its data
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
                    LocalDate.now() // Default to today's date if not provided
                }
            } ?: run {
                // If the habit is not found, set an error message
                errorMessage = "Habit not found."
            }
        } catch (e: Exception) {
            // Log any errors encountered during data fetching
            Log.e("EditHabit", "Error fetching habit: ${e.message}")
            errorMessage = "Error fetching habit." // Update error message
        } finally {
            // Set loading to false once data fetching is complete
            isLoading = false
        }
    }

    // UI displayed while loading the habit data
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator() // Show a loading spinner
                Spacer(modifier = Modifier.height(8.dp)) // Space between elements
                Text("Loading habit...") // Loading text
            }
        }
    } else {
        // If there's an error message, display it
        errorMessage.takeIf { it.isNotBlank() }?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(it, color = MaterialTheme.colorScheme.error) // Show error in red color
            }
        } ?: run {
            // If a habit is found, display the editing interface
            habit?.let {

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fixed TopAppBar
                    TopAppBar(
                        title = {
                            Text("Edit Habit") // Title of the toolbar
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(top = 56.dp)
                            .verticalScroll(rememberScrollState()), // Enable vertical scrolling
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {


                        OutlinedTextField(
                            value = name, // Current habit name
                            onValueChange = { name = it }, // Update name on change
                            label = { Text("Habit Name") }, // Label for the input field
                            modifier = Modifier.fillMaxWidth() // Full width of the screen
                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Space between fields
                        OutlinedTextField(
                            value = description, // Current habit description
                            onValueChange = { description = it }, // Update description on change
                            label = { Text("Description") }, // Label for the input field
                            modifier = Modifier.fillMaxWidth() // Full width of the screen
                        )
                        Spacer(modifier = Modifier.height(16.dp)) // Space between fields

                        // Button to select repeat days
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Button(
                                onClick = { optionState.show() }, // Show repeat days dialog
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(text = "Select Repeat Days") // Button text
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Display selected repeat days
                            Surface(
                                color = MaterialTheme.colorScheme.surface, // Background color
                                shape = RoundedCornerShape(16) // Rounded corners
                            ) {
                                Text(
                                    text = repeat.joinToString(", ")
                                        ?: "No days selected", // Show selected days or default text
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 15.sp // Font size for display
                                )
                            }
                        }

                        // Option dialog for repeat days selection
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
                                } // Update repeat days
                            ),
                            config = OptionConfig(mode = DisplayMode.GRID_VERTICAL) // Display options in a grid
                        )

                        Spacer(modifier = Modifier.height(16.dp)) // Space between fields

                        // Button to select start date
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Button(
                                onClick = { calendarState.show() }, // Show calendar dialog
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(text = "Start From") // Button text
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Display selected start date
                            Surface(
                                modifier = Modifier.padding(top = 16.dp),
                                color = MaterialTheme.colorScheme.surface, // Background color
                                shape = RoundedCornerShape(16) // Rounded corners
                            ) {
                                Text(
                                    text = "Start From: ${startFrom.dayOfMonth}/${startFrom.monthValue}", // Show formatted start date
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 15.sp // Font size for display
                                )
                            }
                        }

                        // Calendar dialog for selecting start date
                        CalendarDialog(
                            state = calendarState,
                            selection = CalendarSelection.Date { date ->
                                startFrom = date // Update start date on selection
                            },
                            config = CalendarConfig() // Configuration for calendar dialog
                        )

                        Spacer(modifier = Modifier.height(16.dp)) // Space between fields

                        // Button to set reminder time
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Button(
                                onClick = { timeState.show() }, // Show clock dialog
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(text = "Set Reminder") // Button text
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Display selected reminder time
                            Surface(
                                modifier = Modifier.padding(top = 16.dp),
                                color = MaterialTheme.colorScheme.surface, // Background color
                                shape = RoundedCornerShape(16) // Rounded corners
                            ) {
                                Text(
                                    text = "Reminder: ${reminderTime?.hour}:${reminderTime?.minute ?: "Not Set"}", // Show reminder time or default text
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 15.sp // Font size for display
                                )
                            }
                        }

                        // Clock dialog for selecting reminder time
                        ClockDialog(
                            state = timeState,
                            selection = ClockSelection.HoursMinutes { hours, minutes ->
                                reminderTime = LocalTime.of(
                                    hours,
                                    minutes
                                ) // Update reminder time on selection
                            },
                            config = ClockConfig() // Configuration for clock dialog
                        )

                        Spacer(modifier = Modifier.height(16.dp)) // Space between fields

                        // Button to save the habit changes
                        val isInputValid =
                            name.isNotBlank() && description.isNotBlank() // Check if inputs are valid
                        Button(
                            onClick = {
                                // Update habit in ViewModel and navigate back
                                habitViewModel.updateHabit(
                                    originalName = habitName,
                                    newName = name,
                                    description = description,
                                    repeat = repeat,
                                    reminder = reminderTime,
                                    startFrom = startFrom,
                                    context = context
                                )
                                navController.popBackStack() // Navigate back after saving
                            },
                            modifier = Modifier.fillMaxWidth(), // Full width of the button
                            enabled = isInputValid // Enable button only if inputs are valid
                        ) {
                            Text("Save Changes") // Button text
                        }
                    }
                } ?: run {
                    // If habit is not found, display a message
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Habit not found.") // Display message
                    }
                }
            }
        }
    }
}