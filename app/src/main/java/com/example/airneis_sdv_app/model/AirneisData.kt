package com.example.airneis_sdv_app.model

import com.example.airneis_sdv_app.R

enum class Categories (
    val title : String,
    val imageIDCat : Int
){
    Bureaux(title = "SALON", imageIDCat = R.drawable.salon),
    Cuisine(title = "CUISINE", imageIDCat = R.drawable.cuisine),
    Chambre(title = "CHAMBRE", imageIDCat = R.drawable.chambre),
}


enum class Products(
    val title: String,
    val description:String,
    val price:String,
    val stock:Int,
    val createdAt:String,
    val updatedAt:String,
    val category:String,
    val imageID: Int,
){
    Table(title = "Table", category = "SALON", imageID = R.drawable.table, description = "Table en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Chaise(title = "Chaise", category = "SALON", imageID = R.drawable.chaise, description = "Chaise en bois", price = "100", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Canape(title = "Canapé", category = "SALON", imageID = R.drawable.canape, description = "Canapé en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Lit(title = "Lit", category = "CHAMBRE", imageID = R.drawable.lit, description = "Lit en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Armoire(title = "Armoire", category = "CHAMBRE", imageID = R.drawable.armoire, description = "Armoire en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Frigo(title = "Frigo", category = "CUISINE", imageID = R.drawable.frigo, description = "Frigo en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Four(title = "Four", category = "CUISINE", imageID = R.drawable.four, description = "Four en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
}