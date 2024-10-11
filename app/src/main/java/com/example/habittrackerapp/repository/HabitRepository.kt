package com.example.habittrackerapp.repository

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
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

   /* fun getHabits(userId: String) {
        val habits = mutableListOf<Habit>()
        db.collection("habits")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val habit = document.toObject(Habit::class.java)
                    habits.add(habit)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("error", "Error getting habits:$exception ")
            }
    }*/

   /* fun getHabits(userId: String): List<Habit> {
        val habits = mutableListOf<Habit>()
        db.collection("users")
            .document(userId)
            .collection("habits")
            .get()
            .addOnSuccessListener {
                result ->
                for (document in result) {
                    val documentSnapshot = document// Your DocumentSnapshot object

                    val habitId = documentSnapshot.id
                    val habitData = documentSnapshot.data!! // Assuming data exists

                    val habit = Habit(
                       *//* id = habitId,*//*
                        userId = habitData["userId"] as String,
                        name = habitData["name"] as String,
                        description = habitData["description"] as String,
                        reminder =
                        if (habitData.containsKey("reminder")) {
                            val reminderData =
                                habitData["reminder"] as Map<String, Any>
                            Reminder(
                                hour = reminderData["hour"] as Int,
                                minute = reminderData["minute"] as Int,
                                second = reminderData["second"] as Int,
                                nano = reminderData["nano"] as Int
                            )
                        }
                        else {
                            null
                        },
                        repeat = (habitData["repeat"] as List<*>).filterIsInstance<String>(),
                        startFrom = habitData["startFrom"] as String
                    )
                   *//* habits.add(habit)*//*

                    Log.d("trace", "Results retrieved: $document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("error", "Error getting habits:$exception") // Use Log.e for errors
            }
        return habits
    }

*/


}