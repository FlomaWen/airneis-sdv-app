package com.example.airneis_sdv_app.component.Account

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import com.example.airneis_sdv_app.component.GlobalApp.ButtonStyled
import com.example.airneis_sdv_app.component.GlobalApp.TextInputStyled

@Composable
fun EditFieldDialog(
    fieldToEdit: String,
    inputValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Modifier $fieldToEdit") },
        text = {
            Column {
                TextInputStyled(
                    value = inputValue,
                    onValueChange = onValueChange,
                    label = { Text(text = fieldToEdit)
                    },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            ButtonStyled(onClick = onSave,text = "Modifier")
        },
        dismissButton = {
            ButtonStyled(onClick = onDismiss,text = "Annuler")
        }
    )
}