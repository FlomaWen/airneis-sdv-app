package com.example.airneis_sdv_app.viewmodel.Account

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.model.User
import com.example.airneis_sdv_app.model.UserResponse
import com.example.airneis_sdv_app.util.Config
import getAccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AccountViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _errorMessage = MutableStateFlow<String?>(null)

    private val client = OkHttpClient()

    fun loadUserFromAPI(context: Context) {
        viewModelScope.launch {
            try {
                val url = "${Config.BASE_URL}/api/user"
                val accessToken = getAccessToken(context)
                val request = Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer $accessToken")
                    .get()
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e("AccountViewModel", "Failed to load user from API", e)
                        _errorMessage.value = "Failed to load user from API: ${e.message}"
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        if (response.isSuccessful) {
                            response.body?.let { responseBody ->
                                try {
                                    val responseJson = responseBody.string()
                                    val userResponse =
                                        Json.decodeFromString<UserResponse>(responseJson)
                                    if (userResponse.success) {
                                        _user.value = userResponse.user
                                    } else {
                                        Log.e(
                                            "AccountViewModel",
                                            "Failed to load user: API response was not successful"
                                        )
                                        _errorMessage.value =
                                            "Failed to load user: API response was not successful"
                                    }
                                } catch (e: Exception) {
                                    Log.e("AccountViewModel", "Failed to parse user response", e)
                                    _errorMessage.value =
                                        "Failed to parse user response: ${e.message}"
                                }
                            }
                        } else {
                            Log.e("AccountViewModel", "Failed to load user: ${response.message}")
                            _errorMessage.value = "Failed to load user: ${response.message}"
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Exception during loadUserFromAPI", e)
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }

    fun updateUser(
        context: Context, name: String, newEmail: String?, currentEmail: String,
        defaultBillingAddressId: Int?, defaultShippingAddressId: Int?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val url = "${Config.BASE_URL}/api/user"
                val accessToken = getAccessToken(context)
                val emailToSend =
                    if (newEmail.isNullOrEmpty() || newEmail == currentEmail) null else newEmail
                val updateUserRequest = buildJsonObject {
                    put("name", name)
                    if (emailToSend != null) put("email", emailToSend)
                    put("defaultBillingAddressId", defaultBillingAddressId)
                    put("defaultShippingAddressId", defaultShippingAddressId)
                }
                val requestBody = updateUserRequest.toString()
                Log.d("AccountViewModel", "Updating user with: $requestBody")

                val request = Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer $accessToken")
                    .patch(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e("AccountViewModel", "Failed to update user", e)
                        _errorMessage.value = "Failed to update user: ${e.message}"
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        val responseBody = response.body?.string()
                        Log.d("AccountViewModel", "Response code: ${response.code}")
                        Log.d("AccountViewModel", "Response body: $responseBody")

                        if (response.isSuccessful) {
                            responseBody?.let {
                                try {
                                    val userResponse = Json.decodeFromString<UserResponse>(it)
                                    if (userResponse.success) {
                                        _user.value = userResponse.user
                                        _errorMessage.value = "User updated successfully"
                                        Log.d(
                                            "AccountViewModel",
                                            "User updated successfully: ${userResponse.user}"
                                        )
                                        onSuccess()
                                    } else {
                                        Log.e(
                                            "AccountViewModel",
                                            "Failed to update user: API response was not successful"
                                        )
                                        _errorMessage.value =
                                            "Failed to update user: API response was not successful"
                                    }
                                } catch (e: Exception) {
                                    Log.e("AccountViewModel", "Failed to parse update response", e)
                                    _errorMessage.value =
                                        "Failed to parse update response: ${e.message}"
                                }
                            } ?: run {
                                Log.e("AccountViewModel", "Response body is null")
                                _errorMessage.value = "Response body is null"
                            }
                        } else {
                            Log.e("AccountViewModel", "Failed to update user: ${response.message}")
                            _errorMessage.value = "Failed to update user: ${response.message}"
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Exception during updateUser", e)
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }

    fun changePassword(
        context: Context,
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val url = "${Config.BASE_URL}/api/user/password"
                val accessToken = getAccessToken(context)
                val requestBody = Json.encodeToString(
                    mapOf(
                        "oldPassword" to oldPassword,
                        "newPassword" to newPassword
                    )
                )
                Log.d("AccountViewModel", "Changing password with: $requestBody")

                val request = Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer $accessToken")
                    .patch(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e("AccountViewModel", "Failed to change passwordaaaa", e)
                        _errorMessage.value = "Failed to change password: ${e.message}"
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        if (response.isSuccessful) {
                            onSuccess()
                        } else {
                            Log.e("AccountViewModel", "Failed to change password: $response")
                            _errorMessage.value = "Failed to change password: ${response.message}"
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Exception during changePassword", e)
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }

}