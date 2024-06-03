package com.example.airneis_sdv_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("role")
    val role: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("defaultBillingAddress")
    val defaultBillingAddress: Int?,
    @SerialName("defaultShippingAddress")
    val defaultShippingAddress: Int?
)

@Serializable
data class UserResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("user")
    val user: User
)
@Serializable
data class UpdateUserRequest(
    val name: String,
    val email: String,
    val defaultBillingAddressId: Int?,
    val defaultShippingAddressId: Int?
)