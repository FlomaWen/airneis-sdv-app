package com.example.airneis_sdv_app.component.GlobalApp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS

@Composable
fun SimpleButtonNav(navController:NavController, destination:String, buttonText:String){
    Button(
        onClick = {
            navController.navigate(destination)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = BlueAIRNEIS
        )
    ) {
        Text(buttonText)
    }
}

