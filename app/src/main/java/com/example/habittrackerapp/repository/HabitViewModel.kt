package com.example.habittrackerapp.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.habittrackerapp.models.Habit
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HabitViewModel: ViewModel() {

    val db = Firebase.firestore
    fun addHabit(
        userId: String,
        name: String,
        description: String,
        repeat: List<String>,
        reminder: LocalTime,
        startFrom: LocalDate,
        currentStreak: Int = 0,
        longestStreak: Int = 0,
        totalPerfectDays: Int = 0,
        totalTimesCompleted: Int = 0,
        isCompletedToday: Boolean = false,
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
            "userId" to userId,
            "currentStreak" to currentStreak,
            "longestStreak" to longestStreak,
            "totalPerfectDays" to totalPerfectDays,
            "totalTimesCompleted" to totalTimesCompleted,
            "isCompletedToday" to isCompletedToday,

            )

        db.collection("users").document(userId).collection("habits").document(habitDocId).set(habit)
            .addOnSuccessListener {
                Toast.makeText(context, "Habit added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    suspend fun getHabitsForUserOnDate(userId: String, date: LocalDate): List<String> {
        return try {
            // Query all habits for the user
            val habitsSnapshot = db.collection("users")
                .document(userId)
                .collection("habits")
                .get()
                .await()

            // Process the documents and filter based on the selected date
            val habits = habitsSnapshot.documents.mapNotNull { document ->
                val startFrom =
                    LocalDate.parse(document.getString("startFrom") ?: return@mapNotNull null)
                val repeat = document.get("repeat") as? List<String> ?: emptyList()

                // Get the day of the week in title case to match Firestore ("Saturday", "Monday", etc.)
                val selectedDayOfWeek =
                    date.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }

                // Check if the habit should be shown on the selected date
                when {
                    // 1. If the habit repeats every day and starts before or on the selected date
                    repeat.contains("Everyday") && startFrom <= date -> {
                        document.getString("name")
                    }
                    // 2. If the habit repeats on specific days (e.g., "Saturday", "Monday") and matches the selected date
                    repeat.contains(selectedDayOfWeek) && startFrom <= date -> {
                        document.getString("name")
                    }
                    // 3. Otherwise, do not show this habit
                    else -> null
                }
            }

            // Return the filtered habit names
            habits

        } catch (e: Exception) {
            // Handle the exception and return an empty list
            emptyList()
        }
    }

    //get all habits names and description for certain user
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