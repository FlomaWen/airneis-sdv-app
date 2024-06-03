package com.example.airneis_sdv_app.component.Product

import Product
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.Caroussels.CarousselData
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS
import com.example.airneis_sdv_app.util.Config
import com.example.airneis_sdv_app.viewmodel.CartViewModel
import isUserLoggedIn

@Composable
fun DetailProduct(product: Product?,navController:NavController) {
    var quantity by remember { mutableIntStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    product?.let {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Product Name
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )

            // Product Description
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )

            // Product Images
            product.images?.let { images ->
                if (images.isNotEmpty()) {
                    CarousselData(
                        images = images.map { image ->
                            "${Config.BASE_URL}/medias/serve/${image.filename}"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .padding(vertical = 8.dp)
                    )
                }
            }

            // Product Price and Stock
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                // Product Price
                Text(
                    text = "Prix : ${product.price} €",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Product Stock
                Text(
                    text = if ((product.stock ?: 0) > 0) "En stock" else "Hors stock",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if ((product.stock ?: 0) > 0) Color.Green else Color.Red
                    ),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }

            // Product Materials
            product.materials?.let { materials ->
                if (materials.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = "Matériaux :",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        materials.forEach { material ->
                            Text(
                                text = material.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .height(30.dp)
                            .width(50.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueAIRNEIS
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("$quantity", fontSize = 10.sp)
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                modifier = Modifier
                                    .size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (1..product.stock!!).forEach { count ->
                            DropdownMenuItem(
                                onClick = {
                                    quantity = count
                                    expanded = false
                                },
                                text = { Text(count.toString()) }
                            )
                        }
                    }
                }

                // Espacement
                Spacer(modifier = Modifier.width(2.dp))

                // Deuxième bouton
                Button(
                    onClick = {
                        if (isUserLoggedIn(context)) {
                            val cartItem = CartViewModel.items.find { it.product.id == product.id }
                            if (cartItem != null) {
                                CartViewModel.modifyQuantityFromAPI(
                                    context,
                                    product.id,
                                    cartItem.quantity + quantity
                                )
                            } else {
                                CartViewModel.addToCartAPI(context, product, quantity)
                            }
                        } else {
                            navController.navigate("LoginScreen") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .height(30.dp)
                        .width(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueAIRNEIS
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Ajouter au panier", fontSize = 10.sp)
                }
            }
        }
    }
}




