package com.example.airneis_sdv_app.model

data class RegistrationUIState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val acceptCookies: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val errorMessage: String? = null
)
data class RegistrationState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val acceptCookies: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val errorMessage: String? = null
)

data class ApiResponseSignup(
    val success: Boolean,
    val message: List<String>,
    val tokens: TokensSignup? = null
)

data class TokensSignup(
    val accessToken: String,
    val refreshToken: String
)