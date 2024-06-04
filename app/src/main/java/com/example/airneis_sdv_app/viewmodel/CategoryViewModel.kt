package com.example.airneis_sdv_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.model.CategoriesResponse
import com.example.airneis_sdv_app.model.Category
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
import android.util.Log
import javax.crypto.AEADBadTagException
import android.security.KeyStoreException

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = URL("${Config.BASE_URL}/api/categories")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.connect()
                val code = connection.responseCode
                println("HTTP Response Code: $code")

                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                val categoriesResponse = Gson().fromJson(response.toString(), CategoriesResponse::class.java)
                _categories.value = categoriesResponse.categories
            } catch (e: AEADBadTagException) {
                Log.e("CryptoError", "AEADBadTagException: ${e.message}")
            } catch (e: KeyStoreException) {
                Log.e("CryptoError", "KeyStoreException: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CategoryViewModel", "Exception: ${e.message}")
            } finally {
                connection.disconnect()
            }
        }
    }
}
