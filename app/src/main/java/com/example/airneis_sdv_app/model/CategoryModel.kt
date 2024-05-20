package com.example.airneis_sdv_app.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class CategoriesResponse(
    val success: Boolean,
    val categories: List<Category>
)

@Serializable
data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("thumbnail")
    val thumbnail: Thumbnail?
)

@Serializable
data class Thumbnail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("filename")
    val filename: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)
