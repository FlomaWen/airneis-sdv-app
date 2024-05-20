package com.example.airneis_sdv_app.viewmodel.Login

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.model.ApiResponseLogin
import com.example.airneis_sdv_app.model.LoginState
import com.example.airneis_sdv_app.model.TokensResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import provideEncryptedSharedPreferences
import saveTokens
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LoginViewModel(private val context:Context) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage = _errorMessage.asStateFlow()


    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> _uiState.value = _uiState.value.copy(email = event.email)
            is LoginUIEvent.PasswordChanged -> _uiState.value = _uiState.value.copy(password = event.password)
            is LoginUIEvent.LoginButtonClicked -> performLogin(context = context)
        }
    }

    private fun performLogin(context: Context) {
        val userData = mapOf(
            "email" to _uiState.value.email,
            "password" to _uiState.value.password,
            "cookies" to false.toString()
        )
        viewModelScope.launch {
            val result = loginUser(userData, context)
            if (!result.success) {
                _errorMessage.value = result.message
            }
            _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = result.success, tokens = result.tokens)
        }
    }

    private suspend fun loginUser(userData: Map<String, String>, context: Context): ApiResponseLogin {
        return withContext(Dispatchers.IO) {
            val url = URL("https://c1bb0d8a5f1d.airneis.net/api/auth/login")
            (url.openConnection() as HttpsURLConnection).run {
                try {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    outputStream.write(Gson().toJson(userData).toByteArray())
                    connect()

                    val responseCode = responseCode
                    val responseMessage = if (responseCode >= HttpsURLConnection.HTTP_OK && responseCode < HttpsURLConnection.HTTP_MULT_CHOICE) {
                        inputStream.bufferedReader().use { it.readText() }
                    } else {
                        errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error occurred"
                    }

                    Log.d("LoginResponse", "Response Code: $responseCode Response: $responseMessage")
                    val response = Gson().fromJson(responseMessage, ApiResponseLogin::class.java)
                    if (response.success && response.tokens != null) {
                        saveTokens(context, response.tokens.accessToken, response.tokens.refreshToken)
                    }
                    response
                } catch (e: Exception) {
                    Log.e("LoginError", "Error during login: ${e.message}")
                    ApiResponseLogin(success = false, message = e.message ?: "An error occurred")
                } finally {
                    disconnect()
                }
            }
        }
    }

    suspend fun refreshAccessToken(context: Context): Boolean {
        val prefs = provideEncryptedSharedPreferences(context)
        val refreshToken = prefs.getString("refreshToken", null) ?: return false

        return withContext(Dispatchers.IO) {
            val url = URL("https://c1bb0d8a5f1d.airneis.net/api/auth/refresh")
            (url.openConnection() as HttpsURLConnection).run {
                try {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    val body = "{\"refreshToken\": \"$refreshToken\"}"
                    outputStream.write(body.toByteArray())
                    connect()

                    val responseCode = responseCode
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        val response = inputStream.bufferedReader().use { it.readText() }
                        val tokens = Gson().fromJson(response, TokensResponse::class.java)
                        tokens.tokens?.let { saveTokens(context, it.accessToken, it.refreshToken) }
                        true
                    } else {
                        false
                    }
                } catch (e: Exception) {
                    false
                } finally {
                    disconnect()
                }
            }
        }
    }




}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

