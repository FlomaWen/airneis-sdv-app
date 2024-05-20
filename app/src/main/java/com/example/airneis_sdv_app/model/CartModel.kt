package com.example.airneis_sdv_app.model

import Product
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val product: Product,
    var quantity: Int
)

@Serializable
data class CartResponse(
    val success: Boolean,
    val basket: List<CartItemAPI>,
    val basketPrice: Double
)

@Serializable
data class CartItemAPI(
    val quantity: Int,
    val createdAt: String,
    val updatedAt: String,
    val product: Product
)
