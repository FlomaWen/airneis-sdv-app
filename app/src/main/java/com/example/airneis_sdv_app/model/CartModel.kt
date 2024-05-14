package com.example.airneis_sdv_app.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val product: Product,
    var quantity: Int
)

