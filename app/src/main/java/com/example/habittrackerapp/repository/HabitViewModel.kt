package com.example.habittrackerapp.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HabitViewModel : ViewModel() {

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
        isReminder: Boolean = false,
        isCompletedToday: Boolean = false,
        context: Context,
    ) {
        // Step 1: Generate a document ID based on userId and habit name
        val habitDocId = "$userId-$name"

        // Step 2: Use document() with the custom document ID
        val habit = hashMapOf(
            "name" to name,
            "isReminder" to isReminder,
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

    suspend fun storeHabitsInMap(userId: String, date: LocalDate): List<Map<String, Boolean>> {
        val habits = mutableListOf<Map<String, Boolean>>()

        try {
            val habitsSnapshot = db.collection("users").document(userId)
                .collection("habits")
                .get()
                .await()

            habitsSnapshot.documents.forEach { document ->
                val startFrom = LocalDate.parse(document.getString("startFrom") ?: return@forEach)
                val repeat = document.get("repeat") as? List<String> ?: emptyList()
                val selectedDayOfWeek =
                    date.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }

                when {
                    repeat.contains("Everyday") && startFrom <= date -> {
                        habits.add(
                            mapOf(
                                document.getString("name")!! to document.getBoolean("isCompletedToday")!!
                            )
                        )
                    }

                    repeat.contains(selectedDayOfWeek) && startFrom <= date -> {
                        habits.add(
                            mapOf(
                                document.getString("name")!! to document.getBoolean("isCompletedToday")!!
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // Handle exception
        }

        return habits
    }

    //get all habits names and description for certain user
//      fun getHabits(userId: String): List<Habit> {
//          val habits = mutableListOf<Habit>()
//          db.collection("users")
//              .document(userId)
//              .collection("habits")
//              .get()
//              .addOnSuccessListener { result ->
//                  for (document in result) {
//                      val documentData = document.data!! // Assuming data exists
//
//                      val habitName = documentData["name"] as String
//                      val habitDescription = documentData["description"] as String
//
//                      habits.add(Habit(name = habitName, description = habitDescription))
//                      Log.d("habits","habit list : $habits")
//                      Log.d("doc","retrieved docs : $document")
//                  }
//              }
//              .addOnFailureListener { exception ->
//                  Log.e("error", "Error getting habits:$exception")
//              }
//          return habits
//      }

    fun deleteHabit(userId: String, habitName: String, context: Context) {
        val habitDocId = "$userId-$habitName"

        db.collection("users").document(userId).collection("habits").document(habitDocId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Habit deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Habit deletion failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun doneHabit(userId: String, habitName: String, context: Context, isDone: Boolean) {
        val habitDocId = "$userId-$habitName"

        db.collection("users").document(userId).collection("habits").document(habitDocId)
            .update("isCompletedToday", isDone)
            .addOnSuccessListener {
                if (isDone)
                    Toast.makeText(context, "Mission Done", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }
}