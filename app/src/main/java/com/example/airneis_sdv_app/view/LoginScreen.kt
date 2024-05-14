package com.example.airneis_sdv_app.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.AppTopBar
import com.example.airneis_sdv_app.component.CustomDrawer
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.NavigationItem
import com.example.airneis_sdv_app.model.isOpened
import com.example.airneis_sdv_app.model.opposite
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS
import com.example.airneis_sdv_app.util.coloredShadow
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import com.example.airneis_sdv_app.viewmodel.Login.LoginUIEvent
import com.example.airneis_sdv_app.viewmodel.Login.LoginViewModel
import com.example.airneis_sdv_app.viewmodel.Login.LoginViewModelFactory
import isUserLoggedIn

@Composable
fun LoginScreen(
    categoryViewModel: CategoryViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context))


    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    val categoryState = categoryViewModel.categories.collectAsState()
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Profile) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidth = configuration.screenWidthDp.dp * density

    val offsetValue = screenWidth / 4.5f
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f
    )

    BackHandler(enabled = drawerState.isOpened()) {
        drawerState = CustomDrawerState.Closed
    }


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CustomDrawer(
                selectedNavigationItem = selectedNavigationItem,
                navController = navController,
                onNavigationItemClick = {
                    selectedNavigationItem = it
                },
                onCloseClick = { drawerState = CustomDrawerState.Closed},
                categories = categoryState.value,
                isUserLoggedIn = isUserLoggedIn(context)
            )
                LoginForm(loginViewModel = loginViewModel,
                    drawerState = drawerState,
                    onDrawerClick = { drawerState = it },
                    modifier = Modifier
                        .offset(x = animatedOffset)
                        .scale(scale = animatedScale)
                        .coloredShadow(
                            color = Color.Black,
                            alpha = 0.1f,
                            shadowRadius = 50.dp
                        ),
                    navController = navController
                    )
        }
    }

@Composable
fun LoginForm(
    loginViewModel: LoginViewModel,
    modifier: Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    navController: NavController
) {
    val uiState = loginViewModel.uiState.collectAsState().value

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - CONNEXION",
                onMenuClick = {
                    onDrawerClick(drawerState.opposite())
                },
                onSearchClick = { /* TODO search */}
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable(onClick = { if (drawerState == CustomDrawerState.Opened) onDrawerClick(CustomDrawerState.Closed) }),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
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
                    Button(
                        onClick = { loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked) },
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




