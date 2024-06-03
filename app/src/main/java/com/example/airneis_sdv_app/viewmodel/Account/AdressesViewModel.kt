package com.example.airneis_sdv_app.viewmodel.Account

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.model.Address
import com.example.airneis_sdv_app.model.AddressAdd
import com.example.airneis_sdv_app.model.AddressResponse
import com.example.airneis_sdv_app.util.Config
import getAccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AddressViewModel : ViewModel() {
    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> get() = _addresses

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading
    private val client = OkHttpClient()


    fun loadAddressesFromAPI(context: Context) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val url = "${Config.BASE_URL}/api/user/addresses"
                val accessToken = getAccessToken(context)
                val request = Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer $accessToken")
                    .get()
                    .build()

                OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e("AddressViewModel", "Failed to load addresses from API", e)
                        _errorMessage.value = "Failed to load addresses from API: ${e.message}"
                        _loading.value = false
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        _loading.value = false
                        if (response.isSuccessful) {
                            response.body?.let { responseBody ->
                                try {
                                    val responseJson = responseBody.string()
                                    val addressResponse = Json.decodeFromString<AddressResponse>(responseJson)
                                    if (addressResponse.success) {
                                        _addresses.value = addressResponse.addresses
                                    } else {
                                        Log.e("AddressViewModel", "Failed to load addresses: API response was not successful")
                                        _errorMessage.value = "Failed to load addresses: API response was not successful"
                                    }
                                } catch (e: Exception) {
                                    Log.e("AddressViewModel", "Failed to parse address response", e)
                                    _errorMessage.value = "Failed to parse address response: ${e.message}"
                                }
                            }
                        } else {
                            Log.e("AddressViewModel", "Failed to load addresses: ${response.message}")
                            _errorMessage.value = "Failed to load addresses: ${response.message}"
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Exception during loadAddressesFromAPI", e)
                _errorMessage.value = "Exception: ${e.message}"
                _loading.value = false
            }
        }
    }

    fun addAddress(context: Context, address: AddressAdd, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val url = "${Config.BASE_URL}/api/user/addresses"
                val accessToken = getAccessToken(context)
                val jsonAddress = Json.encodeToString(address)
                val body = jsonAddress.toRequestBody("application/json".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer $accessToken")
                    .post(body)
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e("AddressViewModel", "Failed to add address", e)
                        _errorMessage.value = "Failed to add address: ${e.message}"
                        _loading.value = false
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        _loading.value = false
                        if (response.isSuccessful) {
                            onSuccess()
                        } else {
                            Log.e("AddressViewModel", "Failed to add address: $response")
                            _errorMessage.value = "Failed to add address: $response"
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Exception during addAddress", e)
                _errorMessage.value = "Exception: ${e.message}"
                _loading.value = false
            }
        }
    }

    fun deleteAddress(context: Context, addressId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val url = "${Config.BASE_URL}/api/user/addresses/$addressId"
                val accessToken = getAccessToken(context)
                val request = Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer $accessToken")
                    .delete()
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e("AddressViewModel", "Failed to delete address", e)
                        _errorMessage.value = "Failed to delete address: ${e.message}"
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        if (response.isSuccessful) {
                            onSuccess()
                        } else {
                            Log.e("AddressViewModel", "Failed to delete address: ${response.message}")
                            _errorMessage.value = "Failed to delete address: ${response.message}"
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Exception during deleteAddress", e)
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }
}
