package com.example.airneis_sdv_app.component.Order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.GlobalApp.AppTopBar
import com.example.airneis_sdv_app.component.GlobalApp.SimpleButtonNav
import com.example.airneis_sdv_app.model.CartItem
import com.example.airneis_sdv_app.model.Drawer.CustomDrawerState
import com.example.airneis_sdv_app.model.Drawer.opposite
import com.example.airneis_sdv_app.viewmodel.CartViewModel

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
                SimpleButtonNav(destination = "payment", navController = navController, buttonText = "Payer")
            }
        }
    }
}