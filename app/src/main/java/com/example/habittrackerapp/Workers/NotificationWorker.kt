import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.habittrackerapp.R

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        // Get the habit name from the input data
        val habitName = inputData.getString("habit_name") ?: "Your Habit"
        sendNotification(habitName)
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(habitName: String) {
        val builder = NotificationCompat.Builder(applicationContext, "1") // Make sure to create the notification channel
            .setSmallIcon(R.drawable.ic_lifestyle) // Your notification icon
            .setContentTitle("Habit Reminder")
            .setContentText("Time to $habitName!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set priority for better visibility
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(1, builder.build()) // Consider making the notification ID dynamic if needed
    }
}
