package com.example.airneis_sdv_app.viewmodel

import Product
import ProductResponse
import ProductsResponse
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.util.Config
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
        categoryId: Int? = null,
        search: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null,
        sortOrder: String? = null,
        order: String? = null,
        materials: List<String>? = null,
        stock: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var currentPage = 1
            var totalPages = 1
            val allProducts = mutableListOf<Product>()

            while (currentPage <= totalPages) {

                val url = buildUrl(categoryId, search, minPrice, maxPrice, sortOrder, order, materials, stock, currentPage)
                Log.d("ProductViewModel", "Request URL: $url")
                Log.d("totalpages", totalPages.toString())
                val connection = url.openConnection() as HttpURLConnection
                try {
                    connection.connect()
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val reader = BufferedReader(InputStreamReader(connection.inputStream))
                        val response = reader.readText()
                        reader.close()

                        Log.d("ProductViewModel", "API Response: $response")

                        val productsResponse = Gson().fromJson(response, ProductsResponse::class.java)
                        totalPages = productsResponse.totalPages

                        // Tri des produits en fonction du prix
                        val sortedProducts = when (sortOrder) {
                            "asc" -> productsResponse.products.sortedBy { it.price }
                            "desc" -> productsResponse.products.sortedByDescending { it.price }
                            else -> productsResponse.products
                        }

                        allProducts.addAll(sortedProducts)
                    } else {
                        Log.e("ProductViewModel", "API Error: ${connection.responseCode}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("ProductViewModel", "Exception: ${e.message}")
                } finally {
                    connection.disconnect()
                }
                currentPage++
            }

            _products.value = allProducts
        }
    }



    private fun buildUrl(
        categoryId: Int?,
        search: String?,
        minPrice: Float?,
        maxPrice: Float?,
        sortOrder: String?,
        order: String?,
        materials: List<String>?,
        stock: String?,
        page: Int
    ): URL {
        val baseUrl = "${Config.BASE_URL}/api/products"
        val parameters = mutableListOf<String>()

        // Add categoryId only if it's not null
        categoryId?.let {
            parameters.add("categories=$it")
        }

        // Add search
        if (!search.isNullOrEmpty()) {
            parameters.add("search=$search")
        }
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

        // Add page
        parameters.add("page=$page")

        // Join all parameters
        val urlWithParams = if (parameters.isNotEmpty()) {
            "$baseUrl?${parameters.joinToString("&")}"
        } else {
            baseUrl
        }

        return URL(urlWithParams)
    }



    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    fun getProductById(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = URL("${Config.BASE_URL}/api/products/$productId")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()

                    Log.d("ProductViewModelById", "API Response: $response")

                    try {
                        val productResponse = Gson().fromJson(response, ProductResponse::class.java)
                        Log.d("ProductViewModelById", "Mapped Product: ${productResponse.product}")
                        _product.value = productResponse.product
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("ProductViewModelById", "Exception: ${e.message}")
                    }

                } else {
                    Log.e("ProductViewModelById", "API Error: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ProductViewModelById", "Exception: ${e.message}")
            } finally {
                connection.disconnect()
            }
        }
    }


}

