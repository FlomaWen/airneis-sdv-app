package com.example.airneis_sdv_app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.airneis_sdv_app.util.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import provideEncryptedSharedPreferences
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LogoutViewModel: ViewModel() {
    private val _logoutSuccessful = MutableStateFlow(false)
    val logoutSuccessful: StateFlow<Boolean> = _logoutSuccessful.asStateFlow()

    fun performLogout(context: Context) {
        viewModelScope.launch {
            if (logoutFromServer(context)) {
                clearTokens(context)
                _logoutSuccessful.value = true
                Log.d("Logout", "Déconnexion réussie et tokens effacés.")
            } else {
                Log.d("Logout", "Échec de la déconnexion.")
            }
        }
    }

    fun resetLogoutState() {
        _logoutSuccessful.value = false
    }

    suspend fun logoutFromServer(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val url = URL("${Config.BASE_URL}/api/auth/logout")
            (url.openConnection() as HttpsURLConnection).run {
                try {
                    requestMethod = "POST"
                    connect()
                    val responseCode = responseCode
                    val responseBody = inputStream.bufferedReader().readText()
                    Log.d("Logout", "Response Code: $responseCode, Response Body: $responseBody")
                    responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED
                } catch (e: Exception) {
                    Log.e("LogoutError", "Error during logout: ${e.message}", e)
                    false
                }
            }
        }
    }



    fun clearTokens(context: Context) {
        val prefs = provideEncryptedSharedPreferences(context)
        prefs.edit().remove("accessToken").remove("refreshToken").apply()
        Log.d("Logout", "Tokens effacés: accessToken et refreshToken")
    }


}
