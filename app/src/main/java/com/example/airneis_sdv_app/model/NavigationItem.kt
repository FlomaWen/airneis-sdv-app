package com.example.airneis_sdv_app.model

import com.example.airneis_sdv_app.R

enum class NavigationItem(
    val title: String,
    val icon: Int,
    val route:String
) {
    Home(
        icon = R.drawable.baseline_home_filled_24,
        title = "Home",
        route = "MainScreen"
    ),
    Profile(
        icon = R.drawable.baseline_person_24,
        title = "Profile",
        route = "ProfileScreen"
    ),
    Settings(
        icon = R.drawable.baseline_settings_24,
        title = "Settings",
        route = "SettingsScreen"
    ),
    Cart(
        icon = R.drawable.baseline_shopping_cart_24,
        title = "Cart",
        route = "CartScreen"
    )
}