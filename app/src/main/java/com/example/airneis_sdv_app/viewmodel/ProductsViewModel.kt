package com.example.airneis_sdv_app.viewmodel

import Product
import ProductsResponse
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun getProducts(
        categoryId: Int,
        search: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null,
        sortOrder: String? = null,
        order: String? = null,
        materials: List<String>? = null,
        stock: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = buildUrl(categoryId, search, minPrice, maxPrice, sortOrder, order, materials, stock)
            Log.d("ProductViewModel", "Request URL: $url")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()

                    Log.d("ProductViewModel", "API Response: $response")

                    val productsResponse = Gson().fromJson(response, ProductsResponse::class.java)

                    // Tri des produits en fonction du prix
                    val sortedProducts = when (sortOrder) {
                        "asc" -> productsResponse.products.sortedBy { it.price }
                        "desc" -> productsResponse.products.sortedByDescending { it.price }
                        else -> productsResponse.products
                    }

                    _products.value = sortedProducts
                } else {
                    Log.e("ProductViewModel", "API Error: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ProductViewModel", "Exception: ${e.message}")
            } finally {
                connection.disconnect()
            }
        }
    }


    private fun buildUrl(
        categoryId: Int,
        search: String?,
        minPrice: Float?,
        maxPrice: Float?,
        sortOrder: String?,
        order: String?,
        materials: List<String>?,
        stock : String?
    ): URL {
        val baseUrl = "https://c1bb0d8a5f1d.airneis.net/api/products"
        val parameters = mutableListOf<String>()

        // Add categoryId
        parameters.add("categories=$categoryId")

        // Add search
        search?.let { parameters.add("search=$it") }

        // Add minPrice
        minPrice?.let { parameters.add("minPrice=$it") }

        // Add maxPrice
        maxPrice?.let { parameters.add("maxPrice=$it") }

        // Add sortOrder and order
        sortOrder?.let { sort ->
            order?.let { ord ->
                parameters.add("sort=$sort")
                parameters.add("order=$ord")
            }
        }

        // Add materials
        materials?.let {
            val materialsParam = it.joinToString(",")
            parameters.add("materials=$materialsParam")
        }

        // Add stock
        stock?.let { parameters.add("stock=$it") }

        // Join all parameters
        val urlWithParams = if (parameters.isNotEmpty()) {
            "$baseUrl?${parameters.joinToString("&")}"
        } else {
            baseUrl
        }

        return URL(urlWithParams)
    }
}
