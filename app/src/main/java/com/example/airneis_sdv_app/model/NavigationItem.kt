package com.example.airneis_sdv_app.model

import com.example.airneis_sdv_app.R

enum class NavigationItem(
    val title: String,
    val icon: Int,
    val route:String
) {
    Home(
        icon = R.drawable.baseline_home_filled_24,
        title = "Accueil",
        route = "MainScreen"
    ),
    Categories(
        icon = R.drawable.baseline_weekend_24,
        title = "Catégories",
        route = "CategoriesScreen"
    ),
    Profile(
        icon = R.drawable.baseline_person_24,
        title = "Profil",
        route = "ProfileScreen"
    ),
    Settings(
        icon = R.drawable.baseline_settings_24,
        title = "Paramètres",
        route = "SettingsScreen"
    ),
    Cart(
        icon = R.drawable.baseline_shopping_cart_24,
        title = "Panier",
        route = "CartScreen"
    )
}