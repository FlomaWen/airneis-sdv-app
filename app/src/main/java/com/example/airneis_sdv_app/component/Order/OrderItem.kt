package com.example.airneis_sdv_app.component.Order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.airneis_sdv_app.model.CartItem

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