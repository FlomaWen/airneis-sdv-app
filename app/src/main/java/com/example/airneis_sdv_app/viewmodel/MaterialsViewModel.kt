package com.example.airneis_sdv_app.viewmodel

import Material
import MaterialsResponse
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.util.Config
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MaterialsViewModel : ViewModel() {
    private val _materials = MutableStateFlow<List<Material>>(emptyList())
    val materials: StateFlow<List<Material>> get() = _materials

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun loadMaterialsFromAPI() {
        viewModelScope.launch {
            val url = "${Config.BASE_URL}/api/materials"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("MaterialsViewModel", "Failed to load materials from API", e)
                    _errorMessage.value = "Failed to load materials from API"
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        response.body?.let { responseBody ->
                            val responseJson = responseBody.string()
                            val materialsResponse = Json.decodeFromString<MaterialsResponse>(responseJson)
                            if (materialsResponse.success) {
                                _materials.value = materialsResponse.materials
                            } else {
                                Log.e("MaterialsViewModel", "Failed to load materials: API response was not successful")
                                _errorMessage.value = "Failed to load materials: API response was not successful"
                            }
                        }
                    } else {
                        Log.e("MaterialsViewModel", "Failed to load materials: ${response.message}")
                        _errorMessage.value = "Failed to load materials: ${response.message}"
                    }
                }
            })
        }
    }
}
