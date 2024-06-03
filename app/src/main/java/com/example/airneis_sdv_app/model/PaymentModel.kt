package com.example.airneis_sdv_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodsResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("paymentMethods") val paymentMethods: List<PaymentMethod>
)

@Serializable
data class PaymentMethod(
    @SerialName("id") val id: Int,
    @SerialName("label") val label: String,
    @SerialName("fullName") val fullName: String,
    @SerialName("expirationMonth") val expirationMonth: Int,
    @SerialName("expirationYear") val expirationYear: Int,
    @SerialName("lastDigits") val lastDigits: String
)