package com.example.airneis_sdv_app.component.GlobalApp

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.airneis_sdv_app.viewmodel.ProductViewModel

@Composable
fun SortByPriceText(
    sortOption: String,
    searchQuery: String,
    minPrice: String,
    maxPrice: String,
    categoryId: Int?,
    selectedMaterials: List<String>,
    isStockChecked: Boolean,
    productViewModel: ProductViewModel,
    onSortOptionChange: (String) -> Unit
) {
    Text(
        text = "Trier par : prix($sortOption)",
        modifier = Modifier.clickable {
            val newSortOption = if (sortOption == "asc") "desc" else "asc"
            onSortOptionChange(newSortOption)
            productViewModel.getProducts(
                search = searchQuery,
                minPrice = minPrice.toFloatOrNull(),
                maxPrice = maxPrice.toFloatOrNull(),
                sortOrder = newSortOption,
                order = null,
                categoryId = categoryId,
                materials = selectedMaterials,
                stock = if (isStockChecked) "1" else "0"
            )
        },
        fontSize = 12.sp
    )
}
