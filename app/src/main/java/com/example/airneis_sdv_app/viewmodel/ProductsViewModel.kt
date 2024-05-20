package com.example.airneis_sdv_app.viewmodel

import Product
import ProductsResponse
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



    fun getProducts(categoryId: Int,
                    search: String? = null,
                    minPrice: Float? = null,
                    maxPrice: Float? = null,
                    sortOrder: String? = null,
                    order: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = buildUrl(categoryId, search, minPrice, maxPrice, sortOrder, order)
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()

                    val productsResponse = Gson().fromJson(response, ProductsResponse::class.java)
                    _products.value = productsResponse.products
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
        order: String?
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

        // Join all parameters
        val urlWithParams = if (parameters.isNotEmpty()) {
            "$baseUrl?${parameters.joinToString("&")}"
        } else {
            baseUrl
        }

        return URL(urlWithParams)
    }

}