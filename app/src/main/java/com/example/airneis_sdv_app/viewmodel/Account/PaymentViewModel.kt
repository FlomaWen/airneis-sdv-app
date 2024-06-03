package com.example.airneis_sdv_app.viewmodel.Account


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.model.PaymentMethod
import com.example.airneis_sdv_app.model.PaymentMethodsResponse
import com.example.airneis_sdv_app.util.Config
import getAccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class PaymentMethodsViewModel : ViewModel() {
    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> get() = _paymentMethods

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading
    private val client = OkHttpClient()

    fun loadPaymentMethodsFromAPI(context: Context) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val url = "${Config.BASE_URL}/api/user/payment-methods"
                val accessToken = getAccessToken(context)
                val request = Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer $accessToken")
                    .get()
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e(
                            "PaymentMethodsViewModel",
                            "Failed to load payment methods from API",
                            e
                        )
                        _errorMessage.value =
                            "Failed to load payment methods from API: ${e.message}"
                        _loading.value = false
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        _loading.value = false
                        if (response.isSuccessful) {
                            response.body?.let { responseBody ->
                                try {
                                    val responseJson = responseBody.string()
                                    val paymentMethodsResponse =
                                        Json.decodeFromString<PaymentMethodsResponse>(responseJson)
                                    if (paymentMethodsResponse.success) {
                                        _paymentMethods.value =
                                            paymentMethodsResponse.paymentMethods
                                    } else {
                                        Log.e(
                                            "PaymentMethodsViewModel",
                                            "Failed to load payment methods: API response was not successful"
                                        )
                                        _errorMessage.value =
                                            "Failed to load payment methods: API response was not successful"
                                    }
                                } catch (e: Exception) {
                                    Log.e(
                                        "PaymentMethodsViewModel",
                                        "Failed to parse payment methods response",
                                        e
                                    )
                                    _errorMessage.value =
                                        "Failed to parse payment methods response: ${e.message}"
                                }
                            }
                        } else {
                            Log.e(
                                "PaymentMethodsViewModel",
                                "Failed to load payment methods: ${response.message}"
                            )
                            _errorMessage.value =
                                "Failed to load payment methods: ${response.message}"
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("PaymentMethodsViewModel", "Exception during loadPaymentMethodsFromAPI", e)
                _errorMessage.value = "Exception: ${e.message}"
                _loading.value = false
            }
        }
    }
}
