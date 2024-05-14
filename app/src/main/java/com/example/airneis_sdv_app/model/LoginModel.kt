package com.example.airneis_sdv_app.model

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val tokens: TokensLogin? = null
)

data class ApiResponseLogin(
    val success: Boolean,
    val message: String,
    val tokens: TokensLogin? = null
)

data class TokensLogin(
    val accessToken: String,
    val refreshToken: String
)

data class TokensResponse(
    val success: Boolean,
    val tokens: TokensLogin? = null
)