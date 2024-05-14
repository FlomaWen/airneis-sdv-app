package com.example.airneis_sdv_app.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.airneis_sdv_app.model.CartItem
import com.example.airneis_sdv_app.model.Product
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CartManager {
    private val items = mutableStateListOf<CartItem>()

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
        loadCart()
    }
    private fun saveCart() {
        try {
            val editor = sharedPreferences.edit()
            val jsonCart = Json.encodeToString(items.toList())
            editor.putString("cartItems", jsonCart)
            editor.apply()
            Log.d("CartManager", "Cart saved successfully")
        } catch (e: Exception) {
            Log.e("CartManager", "Error saving cart", e)
        }
    }

    // loadCart
    fun loadCart() {
        val jsonCart = sharedPreferences.getString("cartItems", null)
        jsonCart?.let {
            val savedItems = Json.decodeFromString<List<CartItem>>(it)
            items.clear()
            items.addAll(savedItems)
        }
    }

    //clear cart
    fun clearCart() {
        items.clear()
        saveCart()
    }

    //add item to cart
    fun addToCart(product: Product, quantity: Int = 1) {
        Log.d("CartManager", "Attempting to add to cart: ${product.name}, Quantity: $quantity")
        val existingItem = items.find { it.product == product }
        if (existingItem != null) {
            Log.d("CartManager", "Product exists in cart. Current quantity: ${existingItem.quantity}")
            existingItem.quantity += quantity
            Log.d("CartManager", "New quantity: ${existingItem.quantity}")
        } else {
            Log.d("CartManager", "Product not found in cart. Adding new item.")
            items.add(CartItem(product, quantity))
        }

        Log.d("CartManager", "Saving cart")
        saveCart()
        Log.d("CartManager", "Cart saved successfully")
    }


    //get Items
    fun getItems(): List<CartItem> = items

    // remove from cart
    fun removeFromCart(cartItem: CartItem) {
        val existingItem = items.find { it.product == cartItem.product }
        if (existingItem != null) {
            items.remove(existingItem)
            saveCart()
        }
    }

}