package com.example.airneis_sdv_app.component.Account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.airneis_sdv_app.model.Address

@Composable
fun AddressCard(address: Address, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
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
                Text(text = "Label: ${address.label}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Nom: ${address.firstName} ${address.lastName}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Adresse: ${address.address1}, ${address.address2}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Ville: ${address.city}, ${address.region}, ${address.postalCode}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Pays: ${address.country}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Téléphone: ${address.phone}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Type: ${address.type}", style = MaterialTheme.typography.bodyMedium)
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
