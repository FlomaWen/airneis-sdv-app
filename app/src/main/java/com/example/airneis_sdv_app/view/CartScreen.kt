package com.example.airneis_sdv_app.view

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.airneis_sdv_app.component.AppTopBar
import com.example.airneis_sdv_app.component.CustomDrawer
import com.example.airneis_sdv_app.viewmodel.CartManager
import com.example.airneis_sdv_app.model.CartItem
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.NavigationItem
import com.example.airneis_sdv_app.model.isOpened
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import com.example.airneis_sdv_app.viewmodel.Login.LoginViewModel
import isUserLoggedIn
import kotlin.math.roundToInt
@Composable
fun CartScreen(navController: NavHostController,categoryViewModel: CategoryViewModel) {
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
            isUserLoggedIn = isUserLoggedIn(LocalContext.current)
        )
        Scaffold(
            modifier = Modifier
                .offset(x = animatedOffset)
                .scale(scale = animatedScale),
            topBar = {
                AppTopBar(
                    title = "ÀIRNEIS - MON PANIER",
                    onMenuClick = { drawerState =
                        if (drawerState.isOpened()) CustomDrawerState.Closed else CustomDrawerState.Opened },
                    onSearchClick = { /* TODO Handle search click */ }
                )
            }
        ) { padding ->
            val items = CartManager.getItems()
            val totalPrice = items.sumOf { it.quantity * it.product.price.toDouble() }
            Column(modifier = Modifier.padding(padding)) {
                if (items.isEmpty()) {
                    Text("Votre panier est vide", style = MaterialTheme.typography.bodyLarge)
                } else {
                    items.forEach { cartItem ->
                        CartItemView(cartItem)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Total : $totalPrice €", style = MaterialTheme.typography.headlineMedium)
                    CheckoutButton(navController, LocalContext.current)
                }
            }
        }
    }
}

@Composable
fun CartItemView(cartItem: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(cartItem.product.name, style = MaterialTheme.typography.bodyMedium)
            Text("Quantité: ${cartItem.quantity}", style = MaterialTheme.typography.labelSmall)
            val totalPriceForItem = cartItem.product.price.toDouble() * cartItem.quantity
            Text("Prix total: $totalPriceForItem €", style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = { CartManager.removeFromCart(cartItem) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}

@Composable
fun CheckoutButton(navController: NavHostController, context: Context) {
    val isLoggedIn = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoggedIn.value = isUserLoggedIn(context)
    }

    if (isLoggedIn.value) {
        Button(
            onClick = {
                navController.navigate("OrderScreen")

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueAIRNEIS
            )
        ) {
            Text("Commander")
        }
    } else {
        Text(
            text = "Veuillez vous connecter pour passer commande.",
            modifier = Modifier.clickable {
                navController.navigate("LoginScreen")
            },
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
            textAlign = TextAlign.Center
        )
    }
}