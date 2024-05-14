package com.example.airneis_sdv_app.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class ProductsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("products")
    val products: List<Product>,

    @SerializedName("limit")
    val limit: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("total")
    val total: Int
)

@Serializable
data class Product(
    @SerializedName("priority")
    val priority: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("stock")
    val stock: Int? = null,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("category")
    val category: Category?,

    @SerializedName("backgroundImage")
    val backgroundImage: backgroundImage?,

    @SerializedName("images")
    val images: List<Image>? = null
)

@Serializable
data class Image(
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
@Serializable
data class backgroundImage(
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
