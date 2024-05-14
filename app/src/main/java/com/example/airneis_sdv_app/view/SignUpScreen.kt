package com.example.airneis_sdv_app.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.example.airneis_sdv_app.model.RegistrationUIState
import com.example.airneis_sdv_app.model.isOpened
import com.example.airneis_sdv_app.model.opposite
import com.example.airneis_sdv_app.util.coloredShadow
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import com.example.airneis_sdv_app.viewmodel.SignUp.SignupUIEvent
import com.example.airneis_sdv_app.viewmodel.SignUp.SignupViewModel
import isUserLoggedIn
import kotlin.math.roundToInt
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS



@Composable
fun SignUpScreen(categoryViewModel: CategoryViewModel, navController: NavController, signupViewModel: SignupViewModel = viewModel()) {
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Home) }
    val uiState = signupViewModel.registrationUIState.collectAsState().value

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Drawer Component
        CustomDrawer(
            selectedNavigationItem = selectedNavigationItem,
            navController = navController,
            onNavigationItemClick = {
                selectedNavigationItem = it
                navController.navigate(it.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                drawerState = CustomDrawerState.Closed
            },
            onCloseClick = { drawerState = CustomDrawerState.Closed },
            categories = categoryViewModel.categories.collectAsState().value,
            isUserLoggedIn = isUserLoggedIn(LocalContext.current)
        )
                RegistrationForm(
                    uiState = uiState,
                    onNameChange = { signupViewModel.onEvent(SignupUIEvent.NameChanged(it)) },
                    onEmailChange = { signupViewModel.onEvent(SignupUIEvent.EmailChanged(it)) },
                    onPasswordChange = { signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it)) },
                    onAcceptCookiesChange = { signupViewModel.onEvent(SignupUIEvent.PrivacyPolicyCheckBoxClicked(it)) },
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
                    signupViewModel = signupViewModel
                )
                if (signupViewModel.signUpInProgress.value) {
                    CircularProgressIndicator()
                }
    }
}

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