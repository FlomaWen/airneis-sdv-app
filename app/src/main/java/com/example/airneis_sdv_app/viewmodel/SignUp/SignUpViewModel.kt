package com.example.airneis_sdv_app.viewmodel.SignUp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.model.ApiResponseSignup
import com.example.airneis_sdv_app.model.RegistrationState
import com.example.airneis_sdv_app.model.RegistrationUIState
import com.example.airneis_sdv_app.util.Config
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection



class SignupViewModel : ViewModel() {
    var registrationUIState = MutableStateFlow(RegistrationUIState())
    var registrationState = mutableStateOf(RegistrationState())
    var signUpInProgress = mutableStateOf(false)

    fun onEvent(event: SignupUIEvent) {
        when (event) {
            is SignupUIEvent.NameChanged -> registrationUIState.value = registrationUIState.value.copy(name = event.name)
            is SignupUIEvent.EmailChanged -> registrationUIState.value = registrationUIState.value.copy(email = event.email)
            is SignupUIEvent.PasswordChanged -> registrationUIState.value = registrationUIState.value.copy(password = event.password)
            is SignupUIEvent.PrivacyPolicyCheckBoxClicked -> registrationUIState.value = registrationUIState.value.copy(acceptCookies = event.status)
            is SignupUIEvent.RegisterButtonClicked -> signUp()
        }
    }
    private fun signUp() {
        val userData = mapOf(
            "name" to registrationUIState.value.name,
            "email" to registrationUIState.value.email,
            "password" to registrationUIState.value.password,
            "cookies" to registrationUIState.value.acceptCookies.toString()
        )
        viewModelScope.launch {
            val result = registerUser(userData)
            registrationState.value = registrationState.value.copy(
                isLoading = false,
                isSuccess = result.success,
                errorMessage = result.message.joinToString("\n")
            )
        }
    }

    // Envoie la requête POST à l'API
    private suspend fun registerUser(userData: Map<String, String>): ApiResponseSignup {
        return withContext(Dispatchers.IO) {
            val url = URL("${Config.BASE_URL}/api/auth/register")
            (url.openConnection() as HttpsURLConnection).run {
                try {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    outputStream.write(Gson().toJson(userData).toByteArray())
                    connect()

                    val responseCode = responseCode
                    val responseMessage = if (responseCode in 200..299) {
                        // Read from inputStream if HTTP OK
                        inputStream.bufferedReader().use { it.readText() }
                    } else {
                        // Read from errorStream if HTTP Error
                        errorStream?.bufferedReader().use { it?.readText() } ?: "Error without body"
                    }
                    Log.d("SignupResponse", "Response Code: $responseCode Response: $responseMessage")
                    parseApiResponse(responseMessage)
                } catch (e: Exception) {
                    Log.e("SignupError", "Error during registration: ${e.message}")
                    ApiResponseSignup(
                        success = false,
                        message = listOf(e.message ?: "An error occurred")
                    )
                } finally {
                    disconnect()
                }
            }
        }
    }

    // Analyse la réponse JSON de l'API
    private fun parseApiResponse(jsonResponse: String): ApiResponseSignup {
        val response = Gson().fromJson(jsonResponse, ApiResponseSignup::class.java)
        if (!response.success) {
            response.message.forEach { errorMessage ->
                Log.e("SignupError", "Error Message: $errorMessage")
            }
            registrationState.value = registrationState.value.copy(
                isSuccess = false,
                errorMessage = response.message.joinToString("\n")
            )
        }
        return response
    }
}