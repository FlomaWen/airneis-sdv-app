package com.example.airneis_sdv_app.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.Account.AccountContent
import com.example.airneis_sdv_app.component.GlobalApp.Drawer.CustomDrawer
import com.example.airneis_sdv_app.model.Drawer.CustomDrawerState
import com.example.airneis_sdv_app.model.Drawer.NavigationItem
import com.example.airneis_sdv_app.model.Drawer.isOpened
import com.example.airneis_sdv_app.util.coloredShadow
import com.example.airneis_sdv_app.viewmodel.Account.AccountViewModel
import com.example.airneis_sdv_app.viewmodel.Account.AddressViewModel
import com.example.airneis_sdv_app.viewmodel.Account.PaymentMethodsViewModel
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import isUserLoggedIn

@Composable
fun AccountScreen(navController: NavController, categoryViewModel: CategoryViewModel,accountViewModel: AccountViewModel,paymentMethodsViewModel: PaymentMethodsViewModel,adressViewModel: AddressViewModel) {

    val context = LocalContext.current
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    val categoriesState = categoryViewModel.categories.collectAsState()
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Account) }
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

    val userState by accountViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        accountViewModel.loadUserFromAPI(context)
        paymentMethodsViewModel.loadPaymentMethodsFromAPI(context)
        adressViewModel.loadAddressesFromAPI(context)
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
            onCloseClick = { drawerState = CustomDrawerState.Closed },
            categories = categoriesState.value,
            isUserLoggedIn = isUserLoggedIn(context)
        )
        AccountContent(
            modifier = Modifier
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.1f,
                    shadowRadius = 50.dp
                ),
            drawerState = drawerState,
            onDrawerClick = { drawerState = it },
            navController = navController,
            user = userState,
            accountViewModel = accountViewModel,
            paymentMethodsViewModel = paymentMethodsViewModel,
            addressViewModel = adressViewModel
        )
    }
}

