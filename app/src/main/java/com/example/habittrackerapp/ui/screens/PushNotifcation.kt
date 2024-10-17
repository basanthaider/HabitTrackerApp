import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    var isPermissionGranted by remember { mutableStateOf(false) }

    // Remember a launcher to request the POST_NOTIFICATIONS permission
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission granted!", Toast.LENGTH_SHORT).show()
            isPermissionGranted = true
        } else {
            Toast.makeText(context, "Permission denied. Notifications won't be sent.", Toast.LENGTH_SHORT).show()
            isPermissionGranted = false
        }
    }

    // Check if we're on Android 13 or above and if the permission is granted
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        )

        // If the permission is already granted, set the state
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true
        }

        // Render the permission request UI only if the permission is not yet granted
        if (!isPermissionGranted) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Notification permission required for sending notifications")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) {
                    Text("Request Permission")
                }
            }
        }
    } else {
        // If below Android 13, we can assume the permission isn't needed or is granted
        isPermissionGranted = true
    }


}
