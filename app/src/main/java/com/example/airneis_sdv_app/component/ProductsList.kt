package com.example.airneis_sdv_app.component

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.airneis_sdv_app.model.CartManager
import com.example.airneis_sdv_app.model.Products

@Composable
fun ProductList(products: List<Products>) {
    LazyColumn {
        item {
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
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: Products) {
    var quantity by remember { mutableStateOf(1) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = product.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = product.imageID),
            contentDescription = "Product image for ${product.title}",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )
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
                    (1..product.stock).forEach { count ->
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
                onClick = { CartManager.addToCart(product, quantity) },
                modifier = Modifier
                    .height(36.dp)
                    .width(120.dp)
            ) {
                Text("Ajouter au panier", fontSize = 12.sp)
            }
        }
    }
}


class ProductPreviewProvider : PreviewParameterProvider<Products> {
    override val values = sequenceOf(
        Products.Table,
        Products.Chaise
    )
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview(@PreviewParameter(ProductPreviewProvider::class) product: Products) {
    ProductItem(product = product)
}