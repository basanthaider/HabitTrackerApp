package com.example.habittrackerapp.models

import java.time.LocalDate
import java.time.LocalTime

data class Habit(
    val habitId: Int,
    val habitName: String,
    val habitDescription: String,
    val repeat : List<String>,
    val reminder : LocalTime,
    val startFrom : LocalDate,
    val userId: String
)
