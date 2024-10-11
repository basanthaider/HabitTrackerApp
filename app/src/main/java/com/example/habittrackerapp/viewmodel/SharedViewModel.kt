package com.example.habittrackerapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapp.models.Habit
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SharedViewModel:ViewModel() {
    /* val state = mutableStateOf(Habit())

    init {
        getHabits(userId = "")
    }
    private fun getHabits(userId: String){
        viewModelScope.launch {
            state.value = getHabitsFromFireStore(userId)
        }
    }
*/
    /*   private var _habitList = MutableStateFlow<List<Habit>>(emptyList())
    var habitList =_habitList.asStateFlow()
    init {
        getHabitList(userId = "")
    }
    fun getHabitList(userId: String){
        var db = Firebase.firestore
        db.collection("users")
            .document(userId)
            .collection("habits")
            .addSnapshotListener { value, error ->
                if (error!=null)
                    return@addSnapshotListener
                if (value!=null)
                    _habitList.value=value.toObjects()
                Log.d("trace","$habitList")
            }


    }


    }

*/
    /*
suspend fun getHabitsFromFireStore( userId: String):Habit{
    val db = FirebaseFirestore.getInstance()
    var habit = Habit()
    try {
        db.collection("users")
            .document(userId)
            .collection("habits")
            .get().await().map {
                val result=  it.toObject(Habit::class.java)
                habit=result
            }
        Log.d("habit","getHabitsFromFireStore $habit")

    }catch (e: FirebaseFirestoreException){
        Log.d("error","getHabitsFromFireStore Exception:$e" )
    }
    return habit
}*/
}
