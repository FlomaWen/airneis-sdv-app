package com.example.airneis_sdv_app.model

import com.example.airneis_sdv_app.R

enum class Categories (
    val title : String,
    val imageIDCat : Int,
    val id: Int
){
    Bureaux(title = "SALON", imageIDCat = R.drawable.salon,id = 1),
    Cuisine(title = "CUISINE", imageIDCat = R.drawable.cuisine,id = 2),
    Chambre(title = "CHAMBRE", imageIDCat = R.drawable.chambre,id = 3),
}


enum class Products(
    val title: String,
    category_id:Int,
    val description:String,
    val price:String,
    val stock:Int,
    val createdAt:String,
    val updatedAt:String,
    val category:String,
    val imageID: Int,
){
    Table(title = "Table", category_id = 1, category = "SALON", imageID = R.drawable.table, description = "Table en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Chaise(title = "Chaise", category_id = 1, category = "SALON", imageID = R.drawable.chaise, description = "Chaise en bois", price = "100", stock = 30, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Canape(title = "Canapé", category_id = 1, category = "SALON", imageID = R.drawable.canape, description = "Canapé en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Lit(title = "Lit", category_id = 3, category = "CHAMBRE", imageID = R.drawable.lit, description = "Lit en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Armoire(title = "Armoire", category_id = 3, category = "CHAMBRE", imageID = R.drawable.armoire, description = "Armoire en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Frigo(title = "Frigo",category_id = 2, category = "CUISINE", imageID = R.drawable.frigo, description = "Frigo en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
    Four(title = "Four",category_id = 2, category = "CUISINE", imageID = R.drawable.four, description = "Four en bois", price = "1000", stock = 10, createdAt = "2021-10-01", updatedAt = "2021-10-01"),
}

