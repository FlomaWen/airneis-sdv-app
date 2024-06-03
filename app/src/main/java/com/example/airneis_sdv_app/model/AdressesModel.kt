package com.example.airneis_sdv_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    @SerialName("id") val id: Int,
    @SerialName("label") val label: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("address1") val address1: String,
    @SerialName("address2") val address2: String,
    @SerialName("city") val city: String,
    @SerialName("region") val region: String,
    @SerialName("postalCode") val postalCode: String,
    @SerialName("country") val country: String,
    @SerialName("phone") val phone: String,
    @SerialName("type") val type: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String
)

@Serializable
data class AddressResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("addresses") val addresses: List<Address>
)

@Serializable
data class AddressAdd(
    @SerialName("label") val label: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("address1") val address1: String,
    @SerialName("address2") val address2: String,
    @SerialName("city") val city: String,
    @SerialName("region") val region: String,
    @SerialName("postalCode") val postalCode: String,
    @SerialName("country") val country: String,
    @SerialName("phone") val phone: String,
    @SerialName("type") val type: String
)

@Serializable
data class AddressDelete(
    @SerialName("success") val success: Boolean
)