package com.example.airneis_sdv_app.component.Product

import Product
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.airneis_sdv_app.R
import com.example.airneis_sdv_app.component.Caroussels.CarousselData
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS
import com.example.airneis_sdv_app.util.Config
import com.example.airneis_sdv_app.viewmodel.CartViewModel
import isUserLoggedIn

@Composable
fun ProductItem(product: Product, navController: NavController, categoryId: Int?) {
    var quantity by remember { mutableIntStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("ProductDetailScreen/${categoryId}/${product.id}")
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFebf2fe)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Product Name and Stock Status
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (product.stock!! > 0) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "En stock",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Green
                        )
                    } else {
                        Text(
                            text = "Hors stock",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Product Image
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

            Spacer(modifier = Modifier.height(8.dp))
            // Price and buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(25.dp))
                Text(
                    text = "${product.price} €",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(125.dp))
                // Right aligned buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box {
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
                    Button(
                        onClick = {
                            if (isUserLoggedIn(context)) {
                                val cartItem = CartViewModel.items.find { it.product.id == product.id }
                                if (cartItem != null) {
                                    CartViewModel.modifyQuantityFromAPI(context, product.id, cartItem.quantity + quantity)
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
                            .width(80.dp)
                            .height(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueAIRNEIS
                        )
                    ) {
                        Text("Ajouter au panier", fontSize = 10.sp)
                    }
                }
            }

            // List materials of product
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                product.materials?.forEachIndexed { index, material ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = material.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black
                        )
                    }
                    if (index < product.materials.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}