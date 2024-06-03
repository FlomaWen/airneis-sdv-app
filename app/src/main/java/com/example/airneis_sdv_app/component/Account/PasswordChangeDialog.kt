package com.example.airneis_sdv_app.component.Account

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import com.example.airneis_sdv_app.component.GlobalApp.ButtonStyled
import com.example.airneis_sdv_app.component.GlobalApp.TextInputStyled

@Composable
fun PasswordChangeDialog(
    oldPassword: TextFieldValue,
    newPassword: TextFieldValue,
    onOldPasswordChange: (TextFieldValue) -> Unit,
    onNewPasswordChange: (TextFieldValue) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var oldPasswordError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }

    val validatePasswords: () -> Boolean = {
        var isValid = true
        if (newPassword.text.length < 8) {
            newPasswordError = "Le mot de passe doit contenir au moins 8 caractères"
            isValid = false
        } else {
            newPasswordError = null
        }
        if (newPassword.text == oldPassword.text) {
            newPasswordError = "Le nouveau mot de passe doit être différent de l'ancien"
            isValid = false
        } else if (newPasswordError == null) {
            newPasswordError = null
        }
        isValid
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Changer de mot de passe") },
        text = {
            Column {
                TextInputStyled(
                    value = oldPassword,
                    onValueChange = {
                        onOldPasswordChange(it)
                        oldPasswordError = null
                    },
                    label = { Text(text = "Ancien mot de passe") },
                    visualTransformation = PasswordVisualTransformation()
                )
                if (oldPasswordError != null) {
                    Text(text = oldPasswordError!!, color = Color.Red)
                }
                TextInputStyled(
                    value = newPassword,
                    onValueChange = {
                        onNewPasswordChange(it)
                        newPasswordError = null
                    },
                    label = { Text(text = "Nouveau mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                )
                if (newPasswordError != null) {
                    Text(text = newPasswordError!!, color = Color.Red)
                }
            }
        },
        confirmButton = {
            ButtonStyled(onClick = {
                if (validatePasswords()) {
                    onSave()
                }
            }, text = "Modifier")
        },
        dismissButton = {
            ButtonStyled(onClick = onDismiss, text = "Annuler")
        }
    )
}
