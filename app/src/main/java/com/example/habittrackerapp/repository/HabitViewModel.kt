package com.example.habittrackerapp.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.habittrackerapp.models.Habit
import com.example.habittrackerapp.models.Reminder
import com.google.firebase.auth.FirebaseAuth
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
        reminder: LocalTime?,
        startFrom: LocalDate,
        currentStreak: Int = 0,
        longestStreak: Int = 0,
        totalPerfectDays: Int = 0,
        totalTimesCompleted: Int = 0,
        isReminder: Boolean = false,
        isCompletedToday: Boolean = false,
        context: Context,
    ) {
        val habitDocId = "$userId-$name"

        val habit = hashMapOf(
            "name" to name,
            "isReminder" to isReminder,
            "description" to description,
            "repeat" to repeat,
            "reminder" to reminder?.toString(), // Convert LocalTime to String
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
                val startFromStr = document.getString("startFrom") ?: return@forEach
                val startFrom = LocalDate.parse(startFromStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val repeat = document.get("repeat")

                        as? List<String> ?: emptyList()
                val selectedDayOfWeek =
                    date.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }

                when {
                    repeat.contains("Everyday") && startFrom <= date -> {
                        habits.add(
                            mapOf(
                                document.getString("name")!! to (document.getBoolean("isCompletedToday") ?: false)
                            )
                        )
                    }

                    repeat.contains(selectedDayOfWeek) && startFrom <= date -> {
                        habits.add(
                            mapOf(
                                document.getString("name")!! to (document.getBoolean("isCompletedToday") ?: false)
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // Handle exception (e.g., log the error)
        }

        return habits
    }

    suspend fun getHabitByName(habitName: String): Habit? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        val habitDocId = "$userId-$habitName"

        return try {
            val document = db.collection("users").document(userId)
                .collection("habits").document(habitDocId).get().await()
            if (document.exists()) {
                val name = document.getString("name") ?: ""
                val description = document.getString("description") ?: ""
                val repeat = document.get("repeat") as? List<String> ?: emptyList()
                val reminderStr = document.getString("reminder")
                val reminder = reminderStr?.let {
                    val parts = it.split(":")
                    if (parts.size >= 2) {
                        LocalTime.of(parts[0].toInt(), parts[1].toInt())
                    } else {
                        null
                    }
                }
                val startFrom = document.getString("startFrom") ?: ""

                Habit(
                    name = name,
                    description = description,
                    reminder = reminder?.let {
                        Reminder(it.hour, it.minute, it.second, it.nano)
                    },
                    repeat = repeat,
                    startFrom = startFrom
                )
            } else null
        } catch (e: Exception) {
            null // Handle exceptions (e.g., log the error)
        }
    }

    fun updateHabit(
        originalName: String, // Original habit name to locate the habit
        newName: String,
        description: String,
        repeat: List<String>,
        reminder: LocalTime?,
        startFrom: LocalDate,
        context: Context
    ) {
        // Retrieve the current user's UID from Firebase Authentication
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        // Create the document ID using the userId and original habit name
        val habitDocId = "$userId-$originalName" // Use originalName to locate the habit

        // First, fetch the isCompletedToday status from the existing document
        db.collection("users").document(userId).collection("habits").document(habitDocId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) { // Check if the document exists
                    // Retrieve the isCompletedToday value, defaulting to false if not present
                    val isCompletedToday = document.getBoolean("isCompletedToday") ?: false

                    // If the name has been changed, delete the old document and add a new one while preserving isCompletedToday
                    if (originalName != newName) {
                        // Create a new document with the new name and preserve isCompletedToday
                        addHabit(
                            userId = userId,
                            name = newName,
                            description = description,
                            repeat = repeat,
                            reminder = reminder,
                            startFrom = startFrom,
                            isCompletedToday = isCompletedToday, // Preserve the status
                            context = context
                        )

                        // Delete the old document
                        deleteHabit(userId, originalName, context)
                    } else {
                        // If the name hasn't changed, update only the other fields
                        val updates = hashMapOf<String, Any>(
                            "description" to description, // Update description
                            "repeat" to repeat, // Update repeat days
                            "reminder" to (reminder?.toString() ?: ""), // Update reminder time or set to empty string if null
                            "startFrom" to startFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) // Update start date with specific format
                        )

                        // Update the existing document with the new values
                        db.collection("users").document(userId).collection("habits").document(habitDocId)
                            .update(updates)
                            .addOnSuccessListener {
                                // Show a success toast message upon successful update
                                Toast.makeText(context, "Habit updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                // Show a failure toast message if the update fails
                                Toast.makeText(context, "Habit update failed", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Show a toast message if the habit does not exist
                    Toast.makeText(context, "Habit does not exist", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                // Show a toast message if there is an error retrieving the habit
                Toast.makeText(context, "Error retrieving habit", Toast.LENGTH_SHORT).show()
            }
    }






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
