package com.example.airneis_sdv_app.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.airneis_sdv_app.component.GlobalApp.AppTopBar
import com.example.airneis_sdv_app.component.CartItemView
import com.example.airneis_sdv_app.component.GlobalApp.Drawer.CustomDrawer
import com.example.airneis_sdv_app.component.GlobalApp.SimpleButtonNav
import com.example.airneis_sdv_app.viewmodel.CartViewModel
import com.example.airneis_sdv_app.model.CartItem
import com.example.airneis_sdv_app.model.Drawer.CustomDrawerState
import com.example.airneis_sdv_app.model.Drawer.NavigationItem
import com.example.airneis_sdv_app.model.Drawer.isOpened
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import isUserLoggedIn
import kotlin.math.roundToInt
@Composable
fun CartScreen(navController: NavHostController, categoryViewModel: CategoryViewModel) {
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Cart) }
    val categoriesState = categoryViewModel.categories.collectAsState()
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp,
        label = "Animated Offset"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
        label = "Animated Scale"
    )

    val context = LocalContext.current
    val isUserLoggedIn = isUserLoggedIn(context)

    var items by remember { mutableStateOf(emptyList<CartItem>()) }
    LaunchedEffect(key1 = CartViewModel.items) {
        CartViewModel.loadCartFromAPI(context) { cartItems ->
            items = cartItems
        }
    }
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        CustomDrawer(
            selectedNavigationItem = selectedNavigationItem,
            navController = navController,
            onNavigationItemClick = {
                selectedNavigationItem = it
                navController.navigate(it.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            onCloseClick = { drawerState = CustomDrawerState.Closed },
            categories = categoriesState.value,
            isUserLoggedIn = isUserLoggedIn
        )
        Scaffold(
            modifier = Modifier
                .offset(x = animatedOffset)
                .scale(scale = animatedScale),
            topBar = {
                AppTopBar(
                    title = "ÀIRNEIS - MON PANIER",
                    onMenuClick = {
                        drawerState = if (drawerState.isOpened()) CustomDrawerState.Closed else CustomDrawerState.Opened
                    },
                    onSearchClick = { navController.navigate("search") }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                if (items.isEmpty()) {
                    Text("Votre panier est vide", style = MaterialTheme.typography.bodyLarge)
                } else {
                    items.forEachIndexed { index, cartItem ->

                        CartItemView(cartItem, context) {
                            CartViewModel.removeFromCartAPI(context, cartItem.product.id)
                            // Force a refresh of the cart items
                            CartViewModel.loadCartFromAPI(context) { cartItems ->
                                items = cartItems
                            }
                        }
                        if (index < items.size - 1) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    val totalPrice = items.sumOf { it.quantity * it.product.price.toDouble() }
                    Text("Total : $totalPrice €", style = MaterialTheme.typography.headlineMedium)
                    SimpleButtonNav(destination = "OrderScreen", navController = navController, buttonText = "Commander")

                }
            }
        }
    }
}

