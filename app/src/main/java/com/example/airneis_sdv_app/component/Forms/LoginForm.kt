package com.example.airneis_sdv_app.component.Forms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.GlobalApp.AppTopBar
import com.example.airneis_sdv_app.model.Drawer.CustomDrawerState
import com.example.airneis_sdv_app.model.Drawer.opposite
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS
import com.example.airneis_sdv_app.viewmodel.Login.LoginUIEvent
import com.example.airneis_sdv_app.viewmodel.Login.LoginViewModel
import isUserLoggedIn

@Composable
fun LoginForm(
    loginViewModel: LoginViewModel,
    modifier: Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    navController: NavController
) {
    val uiState = loginViewModel.uiState.collectAsState().value
    val errorMessage = loginViewModel.errorMessage.collectAsState().value
    val context = LocalContext.current
    val isUserLoggedIn = isUserLoggedIn(context)

    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.navigate("MainScreen")
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - CONNEXION",
                onMenuClick = {
                    onDrawerClick(drawerState.opposite())
                },
                onSearchClick = { navController.navigate("search") }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                        onDrawerClick(CustomDrawerState.Closed)
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = drawerState == CustomDrawerState.Closed) { }
                ) {
                    Text(
                        text = "Heureux de vous revoir",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { loginViewModel.onEvent(LoginUIEvent.EmailChanged(it)) },
                        label = { Text("Email") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it)) },
                        label = { Text("Mot de passe") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Button(
                        onClick = {
                            loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueAIRNEIS
                        )
                    ) {
                        Text("Se connecter")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pas de compte ? S'inscrire",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("SignUpScreen") }
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}

