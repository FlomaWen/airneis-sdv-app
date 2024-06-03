package com.example.airneis_sdv_app

import com.example.airneis_sdv_app.viewmodel.ProductViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.airneis_sdv_app.view.AccountScreen
import com.example.airneis_sdv_app.view.CartScreen
import com.example.airneis_sdv_app.view.LoginScreen
import com.example.airneis_sdv_app.view.MainScreen
import com.example.airneis_sdv_app.view.OrderScreen
import com.example.airneis_sdv_app.view.ProductDetailScreen
import com.example.airneis_sdv_app.view.ProductScreen
import com.example.airneis_sdv_app.view.SearchScreen
import com.example.airneis_sdv_app.view.SignUpScreen
import com.example.airneis_sdv_app.viewmodel.Account.AccountViewModel
import com.example.airneis_sdv_app.viewmodel.Account.AddressViewModel
import com.example.airneis_sdv_app.viewmodel.Account.PaymentMethodsViewModel
import com.example.airneis_sdv_app.viewmodel.CartViewModel
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import com.example.airneis_sdv_app.viewmodel.MaterialsViewModel


class MainActivity : ComponentActivity() {
    private val categoryViewModel by lazy { CategoryViewModel() }
    private val productViewModel by lazy { ProductViewModel() }
    private val materialsViewModel by lazy { MaterialsViewModel() }
    private val accountViewModel by lazy { AccountViewModel() }
    private val paymentMethodsViewModel by lazy { PaymentMethodsViewModel() }
    private val adressViewModel by lazy { AddressViewModel() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CartViewModel.initialize(this)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
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
                                productViewModel = productViewModel,
                                materialsViewModel = materialsViewModel)
                        }
                    }
                    composable("ProductDetailScreen/{categoryId}/{productId}") { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
                        val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                        categoryId?.let {
                            productId?.let {
                                ProductDetailScreen(
                                    navController = navController,
                                    categoryViewModel = categoryViewModel,
                                    categoryId = it,
                                    productId = productId,
                                    productViewModel = productViewModel
                                )
                            }
                        }
                    }
                    composable("search") {
                        SearchScreen(categoryViewModel = categoryViewModel, productViewModel = productViewModel,navController = navController,materialsViewModel = materialsViewModel)
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
                    composable("AccountScreen"){
                        AccountScreen(navController = navController,categoryViewModel = categoryViewModel,accountViewModel = accountViewModel,paymentMethodsViewModel = paymentMethodsViewModel,adressViewModel = adressViewModel)
                    }
                }
            }
        }
    }
}