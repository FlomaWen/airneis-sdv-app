package com.example.airneis_sdv_app.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.airneis_sdv_app.component.AppTopBar
import com.example.airneis_sdv_app.component.CustomDrawer
import com.example.airneis_sdv_app.model.CartManager
import com.example.airneis_sdv_app.model.CartItem
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.NavigationItem
import com.example.airneis_sdv_app.model.isOpened
import kotlin.math.roundToInt

@Composable
fun CartScreen(navController: NavHostController) {
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Cart) }

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
            onCloseClick = { drawerState = CustomDrawerState.Closed }
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
            Column(modifier = Modifier.padding(padding)) {
                if (items.isEmpty()) {
                    Text("Votre panier est vide", style = MaterialTheme.typography.bodyLarge)
                } else {
                    items.forEach { cartItem ->
                        CartItemView(cartItem)
                    }
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
        Text(cartItem.product.title, style = MaterialTheme.typography.bodyMedium)
        Text("Quantité: ${cartItem.quantity}", style = MaterialTheme.typography.labelSmall)
        IconButton(onClick = { CartManager.removeItem(cartItem) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}
