package com.example.airneis_sdv_app.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.CustomDrawer
import com.example.airneis_sdv_app.component.RegistrationForm
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.NavigationItem
import com.example.airneis_sdv_app.model.isOpened
import com.example.airneis_sdv_app.util.coloredShadow
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import com.example.airneis_sdv_app.viewmodel.SignUp.SignupUIEvent
import com.example.airneis_sdv_app.viewmodel.SignUp.SignupViewModel
import isUserLoggedIn
import kotlin.math.roundToInt


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


