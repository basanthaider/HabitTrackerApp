package com.example.habittrackerapp.repository

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
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

        db.collection("habits").document(habitDocId).set(habit)
            .addOnSuccessListener {
                Toast.makeText(context, "Habit added successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("/home")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }
    // Fetch habits for a specific user on a specific date
    suspend fun getHabitsForUserOnDate(userId: String, date: LocalDate): List<String> {
        return try {
            val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val querySnapshot = db.collection("habits")
                .whereEqualTo("userId", userId)
                .whereEqualTo("startFrom", formattedDate)
                .get()
                .await()

            // Process the documents and return a list of habits
            querySnapshot.documents.mapNotNull { document ->
                document.getString("name")
            }
        } catch (e: Exception) {
            emptyList() // Return an empty list if something goes wrong
        }
    }
}

