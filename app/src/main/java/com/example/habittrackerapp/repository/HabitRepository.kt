package com.example.habittrackerapp.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavHostController
import com.example.habittrackerapp.models.Habit
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HabitRepository {
    val db = Firebase.firestore
    fun addHabit(
        userId: String,
        name: String,
        description: String,
        repeat: List<String>,
        reminder: LocalTime,
        startFrom: LocalDate,
        context: Context,
        navController: NavHostController
    ) {
        // Step 1: Generate a document ID based on userId and habit name
        val habitDocId = "$userId-$name"

        // Step 2: Use document() with the custom document ID
        val habit = hashMapOf(
            "name" to name,
            "description" to description,
            "repeat" to repeat,
            "reminder" to reminder,
            "startFrom" to startFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            "userId" to userId
        )

        db.collection("users").document(userId).collection("habits").document(habitDocId).set(habit)
            .addOnSuccessListener {
                Toast.makeText(context, "Habit added successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("/home")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }


    //only name and description
    fun getHabits(userId: String): List<Habit> {
        val habits = mutableListOf<Habit>()
        db.collection("users")
            .document(userId)
            .collection("habits")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val documentData = document.data!! // Assuming data exists

                    val habitName = documentData["name"] as String
                    val habitDescription = documentData["description"] as String

                    habits.add(Habit(name = habitName, description = habitDescription))
                    Log.d("habits","habit list : $habits")
                    Log.d("doc","retrieved docs : $document")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("error", "Error getting habits:$exception")
            }
        return habits
    }



}