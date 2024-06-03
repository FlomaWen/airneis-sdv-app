package com.example.airneis_sdv_app.component.GlobalApp

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS

@Composable
fun ButtonStyled(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = BlueAIRNEIS
        )
    ) {
        Text(text)
    }
}