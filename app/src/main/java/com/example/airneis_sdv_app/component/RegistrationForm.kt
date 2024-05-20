package com.example.airneis_sdv_app.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.RegistrationUIState
import com.example.airneis_sdv_app.model.opposite
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS
import com.example.airneis_sdv_app.viewmodel.SignUp.SignupUIEvent
import com.example.airneis_sdv_app.viewmodel.SignUp.SignupViewModel

@Composable
fun RegistrationForm(
    uiState: RegistrationUIState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onAcceptCookiesChange: (Boolean) -> Unit,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    modifier: Modifier,
    signupViewModel: SignupViewModel
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - INSCRIPTION",
                onMenuClick = {
                    onDrawerClick(drawerState.opposite())
                },
                onSearchClick = { /* TODO Handle search */ }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable(onClick = {
                        if (drawerState == CustomDrawerState.Opened) onDrawerClick(
                            CustomDrawerState.Closed
                        )
                    }),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        "Bienvenue !",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = onNameChange,
                        label = { Text("Nom d'utilisateur") },
                    )
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        label = { Text("Email") },
                    )
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        label = { Text("Mot de passe") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = uiState.acceptCookies,
                            onCheckedChange = onAcceptCookiesChange
                        )
                        Text(text = "Accepter les cookies")
                    }
                    Button(
                        onClick = { signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueAIRNEIS
                        )
                    ) {
                        Text("S'inscrire")
                    }
                }
            }
        }
    )
}