package com.example.airneis_sdv_app

import ProductViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.airneis_sdv_app.view.CartScreen
import com.example.airneis_sdv_app.view.LoginScreen
import com.example.airneis_sdv_app.view.MainScreen
import com.example.airneis_sdv_app.view.OrderScreen
import com.example.airneis_sdv_app.view.ProductScreen
import com.example.airneis_sdv_app.view.SignUpScreen
import com.example.airneis_sdv_app.viewmodel.CartManager
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel


class MainActivity : ComponentActivity() {
    private val categoryViewModel by lazy { CategoryViewModel() }
    private val productViewModel by lazy { ProductViewModel() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CartManager.init(this)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val categories = categoryViewModel.categories.collectAsState().value
                NavHost(navController = navController, startDestination = "mainScreen") {
                    composable("mainScreen") {
                        MainScreen(navController = navController,categoryViewModel = categoryViewModel)
                    }
                    composable("productScreen/{categoryId}") { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
                        categoryId?.let {
                            ProductScreen(
                                categoryId = it, navController = navController,
                                categoryViewModel = categoryViewModel,
                                productViewModel = productViewModel)
                        }
                    }
                    composable("CartScreen"){
                        CartScreen(navController = navController,categoryViewModel = categoryViewModel)
                    }
                    composable("SignUpScreen"){
                        SignUpScreen(navController = navController,categoryViewModel = categoryViewModel)
                    }
                    composable("LoginScreen"){
                        LoginScreen(navController = navController,categoryViewModel = categoryViewModel)
                    }
                    composable("OrderScreen"){
                        OrderScreen(navController = navController,categoryViewModel = categoryViewModel)
                    }
                }
            }
        }
    }
}