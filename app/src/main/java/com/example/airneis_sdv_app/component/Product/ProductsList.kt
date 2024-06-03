package com.example.airneis_sdv_app.component.Product

import Product
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.airneis_sdv_app.R
import com.example.airneis_sdv_app.component.GlobalApp.SortByPriceText
import com.example.airneis_sdv_app.viewmodel.MaterialsViewModel
import com.example.airneis_sdv_app.viewmodel.ProductViewModel

@Composable
fun ProductList(
    products: List<Product>,
    productViewModel: ProductViewModel,
    categoryId: Int?,
    navController: NavController,
    materialsViewModel: MaterialsViewModel,
    searchQuery: String
) {
    if (products.isEmpty()) {
        Text(
            text = "Aucun produit trouvé",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn {
            item {
                var isDialogOpen by remember { mutableStateOf(false) }
                var sortOption by remember { mutableStateOf("desc") }
                var minPrice by remember { mutableStateOf("") }
                var maxPrice by remember { mutableStateOf("") }
                val selectedMaterials by remember { mutableStateOf<MutableList<String>>(mutableListOf()) }
                var isStockChecked by remember { mutableStateOf(true) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Filtrer", modifier = Modifier.padding(start = 8.dp))
                            IconButton(onClick = { isDialogOpen = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                                    contentDescription = "Filtrer",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(32.dp))
                        SortByPriceText(
                            sortOption = sortOption,
                            searchQuery = searchQuery,
                            minPrice = minPrice,
                            maxPrice = maxPrice,
                            categoryId = categoryId,
                            selectedMaterials = selectedMaterials,
                            isStockChecked = isStockChecked,
                            productViewModel = productViewModel,
                            onSortOptionChange = { newSortOption ->
                                sortOption = newSortOption
                            }
                        )
                    }
                }

                if (isDialogOpen) {
                    Dialog(onDismissRequest = { isDialogOpen = false }) {
                        Box(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            ProductsFilters(
                                productViewModel = productViewModel,
                                categoryId = categoryId,
                                materialsViewModel = materialsViewModel,
                                initialMinPrice = minPrice,
                                initialMaxPrice = maxPrice,
                                initialSelectedMaterials = selectedMaterials,
                                initialIsStockChecked = isStockChecked,
                                sortOption = sortOption,
                                onCloseDialog = { filterState ->
                                    minPrice = filterState.minPrice
                                    maxPrice = filterState.maxPrice
                                    selectedMaterials.clear()
                                    selectedMaterials.addAll(filterState.selectedMaterials)
                                    isStockChecked = filterState.isStockChecked
                                    isDialogOpen = false
                                }
                            )
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
                ProductItem(product, navController, categoryId)
            }
        }
    }
}


