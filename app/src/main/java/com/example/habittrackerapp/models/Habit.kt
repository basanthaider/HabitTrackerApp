package com.example.habittrackerapp.models

data class Habit(
    /*val id: String, // Unique habit ID
    val userId: String,*/
    val name: String = "",
    val description: String = "",
    val reminder: Reminder?, // Nullable for habits without reminders
    val repeat: List<String>, // e.g., ["Everyday", "Weekdays"]
    val startFrom: String // Date string (e.g., "2024-10-10")*/
)


data class Reminder(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
)

