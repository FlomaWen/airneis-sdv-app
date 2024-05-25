package com.example.airneis_sdv_app.data

data class FilterState(
    val minPrice: String,
    val maxPrice: String,
    val selectedMaterials: List<String>,
    val isStockChecked: Boolean
)