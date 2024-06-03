package com.example.airneis_sdv_app.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CategoriesResponse(
    val success: Boolean,
    val categories: List<Category>
)

@Serializable
data class Category(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("thumbnail")
    val thumbnail: Thumbnail?
)

@Serializable
data class Thumbnail(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("filename")
    val filename: String,
    @SerialName("type")
    val type: String,
    @SerialName("size")
    val size: Int,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)
