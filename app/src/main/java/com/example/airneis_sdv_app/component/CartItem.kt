package com.example.airneis_sdv_app.component

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.airneis_sdv_app.model.CartItem
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS

@Composable
fun CartItemView(cartItem: CartItem, context: Context, onDeleteClick: () -> Unit) {
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

        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}

@Composable
fun CheckoutButton(navController: NavHostController) {
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
}
