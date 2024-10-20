package com.example.habittrackerapp.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.habittrackerapp.R

@Composable
 fun PermissionDeniedDialog(onActionClicked: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        confirmButton = {
            TextButton(onClick = { onActionClicked(true) }) {
                Text(text = "Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = { onActionClicked(false) }) {
                Text(text = "Don't ask again")
            }
        },
        icon = {
            Icon(
                painter = painterResource(R.drawable.alert),
                contentDescription = "Warning"
            )
        },
        title = {
            Text(text = "Permission Needed")
        },
        text = {
            Text(text = "Enable notifications to activate reminders and stay on track with your habits!")
        }
    )
}