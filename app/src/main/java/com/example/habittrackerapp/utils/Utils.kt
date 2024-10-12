package com.example.habittrackerapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDataForChart(dataInMillies:Long):String{
        val dataFormatter =SimpleDateFormat(("dd-MM"),Locale.getDefault())
        return dataFormatter.format(dataInMillies)
    }

}
object UserSession {
    var userId: String = ""
}
