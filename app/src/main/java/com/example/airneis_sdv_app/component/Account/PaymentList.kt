package com.example.airneis_sdv_app.component.Account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.airneis_sdv_app.model.PaymentMethod

@Composable
fun PaymentMethodCard(paymentMethod: PaymentMethod, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFebf2fe)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(text = "Label: ${paymentMethod.label}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Nom: ${paymentMethod.fullName}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Expiration: ${paymentMethod.expirationMonth}/${paymentMethod.expirationYear}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Derniers chiffres: ${paymentMethod.lastDigits}", style = MaterialTheme.typography.bodyMedium)
            }
            Column(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Red)
                }
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Modifier", tint = Color.Black)
                }
            }
        }
    }
}