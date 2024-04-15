package com.example.airneis_sdv_app.model

data class CartItem(
    val product: Products,
    var quantity: Int
)

object CartManager {
    private val items = mutableListOf<CartItem>()

    fun addToCart(product: Products, quantity: Int = 1) {
        val existingItem = items.find { it.product == product }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            items.add(CartItem(product, quantity))
        }
    }

    fun getItems(): List<CartItem> = items

    fun removeItem(cartItem: CartItem) {
        items.remove(cartItem)
    }

    fun clearCart() {
        items.clear()
    }
}