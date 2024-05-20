package com.example.airneis_sdv_app.component

import Product
import com.example.airneis_sdv_app.viewmodel.ProductViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.airneis_sdv_app.R
import com.example.airneis_sdv_app.viewmodel.CartViewModel
import com.example.airneis_sdv_app.viewmodel.MaterialsViewModel
import isUserLoggedIn

@Composable
fun ProductList(products: List<Product>, categoryId: Int, productViewModel: ProductViewModel, navController: NavController,materialsViewModel: MaterialsViewModel) {
    LazyColumn {
        item {
            var isDialogOpen by remember { mutableStateOf(false) }
            var sortOption by remember { mutableStateOf("asc") }
            var minPrice by remember { mutableStateOf("") }
            var maxPrice by remember { mutableStateOf("") }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Filtrer")
                IconButton(onClick = { isDialogOpen = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                        contentDescription = "Filtrer",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Trier par : prix($sortOption)",
                    modifier = Modifier.clickable {
                        sortOption = if (sortOption == "asc") "desc" else "asc"
                        productViewModel.getProducts(
                            minPrice = minPrice.toFloatOrNull(),
                            maxPrice = maxPrice.toFloatOrNull(),
                            sortOrder = sortOption,
                            order = null,
                            categoryId = categoryId
                        )
                    },
                    fontSize = 12.sp
                )
            }

            if (isDialogOpen) {
                Dialog(onDismissRequest = { isDialogOpen = false }) {
                    // Ajoutez un fond blanc au dialogue
                    Box(modifier = Modifier.background(Color.White).padding(16.dp)) {
                        ProductsFilters(productViewModel, categoryId,materialsViewModel,minPrice, maxPrice, sortOption)
                    }
                }
            }
            Text(
                text = "NOS PRODUITS",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
        items(products) { product ->
            ProductItem(product,navController)
        }
    }
}

@Composable
fun ProductItem(product: Product, navController: NavController) {
    var quantity by remember { mutableIntStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        product.images?.forEach { image ->
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = "https://c1bb0d8a5f1d.airneis.net/medias/serve/${image.filename}")
                        .apply(block = fun ImageRequest.Builder.() {
                            error(R.drawable.baseline_error_24)
                        }).build()
                ),
                contentDescription = "Product Image",
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "${product.price} €",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .height(36.dp)
                    ) {
                        Text("$quantity", fontSize = 14.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
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
                Spacer(modifier = Modifier.width(24.dp))
                Button(
                    onClick = {
                        if (isUserLoggedIn(context)) {
                            val cartItem = CartViewModel.items.find { it.product.id == product.id }
                            if (cartItem != null) {
                                CartViewModel.modifyQuantityFromAPI(context, product.id, cartItem.quantity + quantity)
                            } else {
                                CartViewModel.addToCartAPI(context, product, quantity)
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
                        .height(36.dp)
                        .width(120.dp)
                ) {
                    Text("Ajouter au panier", fontSize = 12.sp)
                }
            }
    }
}