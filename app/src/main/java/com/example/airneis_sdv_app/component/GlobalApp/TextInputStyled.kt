package com.example.airneis_sdv_app.component.GlobalApp

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS

@Composable
fun TextInputStyled(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: @Composable () -> Unit,
    visualTransformation: PasswordVisualTransformation? = null
) {
    if (visualTransformation != null) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = BlueAIRNEIS,
                focusedBorderColor = BlueAIRNEIS,
                unfocusedBorderColor = BlueAIRNEIS,
                focusedLabelColor = BlueAIRNEIS,
                focusedTrailingIconColor = BlueAIRNEIS,
                focusedContainerColor = Color.Transparent,
                focusedLeadingIconColor = BlueAIRNEIS,
                focusedPlaceholderColor = BlueAIRNEIS,
                selectionColors = TextSelectionColors(
                    handleColor = BlueAIRNEIS,
                    backgroundColor = BlueAIRNEIS
                )
            ),
            visualTransformation = visualTransformation
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = BlueAIRNEIS,
                focusedBorderColor = BlueAIRNEIS,
                unfocusedBorderColor = BlueAIRNEIS,
                focusedLabelColor = BlueAIRNEIS,
                focusedTrailingIconColor = BlueAIRNEIS,
                focusedContainerColor = Color.Transparent,
                focusedLeadingIconColor = BlueAIRNEIS,
                focusedPlaceholderColor = BlueAIRNEIS,
                selectionColors = TextSelectionColors(
                    handleColor = BlueAIRNEIS,
                    backgroundColor = BlueAIRNEIS
                )
            )
        )
    }
}
