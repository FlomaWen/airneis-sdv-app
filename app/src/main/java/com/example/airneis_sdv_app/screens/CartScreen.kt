package com.example.airneis_sdv_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.airneis_sdv_app.model.CartManager
import com.example.airneis_sdv_app.model.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mon Panier") }) }
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

@Composable
fun CartItemView(cartItem: CartItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(cartItem.product.title, style = MaterialTheme.typography.bodyMedium)
        Text("Quantité: ${cartItem.quantity}", style = MaterialTheme.typography.labelSmall)
        IconButton(onClick = { CartManager.removeItem(cartItem) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}
