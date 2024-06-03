package com.example.airneis_sdv_app.viewmodel

import Product
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.airneis_sdv_app.model.CartItem
import com.example.airneis_sdv_app.model.CartResponse
import com.example.airneis_sdv_app.util.Config
import getAccessToken
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

object CartViewModel: ViewModel() {
    val items = mutableListOf<CartItem>()
    private const val PREF_CART_ITEMS = "pref_cart_items"

    fun initialize(context: Context) {
        val sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        val cartItemsJson = sharedPreferences.getString(PREF_CART_ITEMS, null)
        if (cartItemsJson != null) {
            items.addAll(Json.decodeFromString<List<CartItem>>(cartItemsJson))
        }
    }


    fun addToCartAPI(context: Context, product: Product, quantity: Int) {
        val url = "${Config.BASE_URL}/api/user/basket"
        val jsonBody = Json.encodeToString(mapOf("productId" to product.id, "quantity" to quantity))
        val requestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())

        val accessToken = getAccessToken(context)
        if (accessToken == null) {
            Log.e("CartViewModel", "Failed to add item to cart: No access token available")
            return
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CartManager", "Failed to add item to cartaaaaaa", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("CartViewModel", "Item added to cart successfully")
                    loadCartFromAPI(context) { cartItems ->
                        items.clear()
                        items.addAll(cartItems)
                    }
                    saveCartToPrefs(context)
                } else {
                    Log.e("CartViewModel", "Failed to add item to cartzzzzz: ${response.message}")
                    response.body?.let {
                        Log.e("CartViewModel", "Response body: ${it.string()}")
                    }
                }
            }
        })
    }

    fun loadCartFromAPI(context: Context, callback: (List<CartItem>) -> Unit) {
        val url = "${Config.BASE_URL}/api/user/basket"

        val accessToken = getAccessToken(context)
        if (accessToken == null) {
            Log.e("CartViewModel", "Failed to load cart from API: No access token available")
            return
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CartViewModel", "Failed to load cart from API", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val responseJson = responseBody.string()
                        val cartResponse = Json.decodeFromString<CartResponse>(responseJson)
                        val cartItems = cartResponse.basket.map { CartItem(it.product, it.quantity) }
                        callback(cartItems)
                        Log.d("CartViewModel", "Cart loaded from API successfully")
                    }

                } else {
                    Log.e("CartViewModel", "Failed to load cart from API: ${response.message}")
                }
            }
        })
    }

    fun removeFromCartAPI(context: Context, productId: Int) {
        val url = "${Config.BASE_URL}/api/user/basket"
        val jsonBody = Json.encodeToString(mapOf("productId" to productId))
        val requestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())

        val accessToken = getAccessToken(context)
        if (accessToken == null) {
            Log.e("CartViewModel", "Failed to remove item from cart: No access token available")
            return
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .delete(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CartViewModel", "Failed to remove item from cart", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("CartViewModel", "Item removed from cart successfully")
                    // Charger à nouveau le panier après la suppression
                    loadCartFromAPI(context) { cartItems ->
                        items.clear()
                        items.addAll(cartItems)
                    }
                    saveCartToPrefs(context)

                } else {
                    Log.e("CartViewModel", "Failed to remove item from cart: ${response.message}")
                }
            }
        })
    }

    fun modifyQuantityFromAPI(context: Context, productId: Int, quantity: Int) {
        val url = "${Config.BASE_URL}/api/user/basket"
        val jsonBody = Json.encodeToString(mapOf("productId" to productId, "quantity" to quantity))
        val requestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())

        val accessToken = getAccessToken(context)
        if (accessToken == null) {
            Log.e("CartViewModel", "Failed to modify quantity: No access token available")
            return
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .patch(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CartViewModel", "Failed to modify quantity", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("CartManager", "Quantity modified successfully")
                    loadCartFromAPI(context) { cartItems ->
                        items.clear()
                        items.addAll(cartItems)
                    }
                    saveCartToPrefs(context)

                } else {
                    Log.e("CartViewModel", "Failed to modify quantity: ${response.message}")
                }
            }
        })
    }
    private fun saveCartToPrefs(context: Context) {
        val sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(PREF_CART_ITEMS, Json.encodeToString(items)).apply()
    }
}
