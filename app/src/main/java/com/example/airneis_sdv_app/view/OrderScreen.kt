package com.example.airneis_sdv_app.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.airneis_sdv_app.model.CartItem
import com.example.airneis_sdv_app.viewmodel.CartViewModel
import com.example.airneis_sdv_app.component.AppTopBar
import com.example.airneis_sdv_app.component.CustomDrawer
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.NavigationItem
import com.example.airneis_sdv_app.model.isOpened
import com.example.airneis_sdv_app.model.opposite
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS
import com.example.airneis_sdv_app.util.coloredShadow
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import isUserLoggedIn
import kotlin.math.roundToInt

@Composable
fun OrderScreen(navController: NavHostController,
                categoryViewModel: CategoryViewModel
) {
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
    BackHandler(enabled = drawerState.isOpened()) {
        drawerState = CustomDrawerState.Closed
    }

    val categoryState = categoryViewModel.categories.collectAsState()

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
            },
            onCloseClick = { drawerState = CustomDrawerState.Closed},
            categories = categoryState.value,
            isUserLoggedIn = isUserLoggedIn(LocalContext.current)
        )
        OrderContent(navController = navController,modifier = Modifier
            .offset(x = animatedOffset)
            .scale(scale = animatedScale)
            .coloredShadow(
                color = Color.Black,
                alpha = 0.1f,
                shadowRadius = 50.dp
            ),
            drawerState = drawerState, onDrawerClick = { drawerState = it })
    }
}



@Composable
fun OrderContent(
    navController: NavController,
    modifier: Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit
) {
    Scaffold(
        modifier = modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - COMMANDE",
                onMenuClick = { onDrawerClick(drawerState.opposite()) },
                onSearchClick = { /* Handle search click */ }
            )
        }
    ) { paddingValues ->
        val context = LocalContext.current
        var items by remember { mutableStateOf(emptyList<CartItem>()) }

        CartViewModel.loadCartFromAPI(context) { cartItems ->
            items = cartItems
        }

        val total = items.sumOf { it.quantity * it.product.price.toDouble() }

        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            if (items.isEmpty()) {
                Text("Votre panier est vide")
            } else {
                Text("Résumé de la commande", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                items.forEach { item ->
                    OrderItemView(item)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Total à payer : $total €", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {

                        navController.navigate("SuccessScreen")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueAIRNEIS
                    )
                ) {
                    Text("Passer la commande")
                }
            }
        }
    }
}


@Composable
fun OrderItemView(cartItem: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${cartItem.product.name} x ${cartItem.quantity}", style = MaterialTheme.typography.bodyLarge)
        val itemTotal = cartItem.quantity * cartItem.product.price.toDouble()
        Text("$itemTotal €", style = MaterialTheme.typography.bodyLarge)
    }
}
