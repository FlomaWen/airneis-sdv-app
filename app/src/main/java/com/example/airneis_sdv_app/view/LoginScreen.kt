package com.example.airneis_sdv_app.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.airneis_sdv_app.component.LoginForm
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.NavigationItem
import com.example.airneis_sdv_app.model.isOpened
import com.example.airneis_sdv_app.util.coloredShadow
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
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
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp, label = ""
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f, label = ""
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

