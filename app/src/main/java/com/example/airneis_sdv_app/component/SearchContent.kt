package com.example.airneis_sdv_app.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.GlobalApp.AppTopBar
import com.example.airneis_sdv_app.component.Product.ProductList
import com.example.airneis_sdv_app.model.Drawer.CustomDrawerState
import com.example.airneis_sdv_app.model.Drawer.opposite
import com.example.airneis_sdv_app.viewmodel.MaterialsViewModel
import com.example.airneis_sdv_app.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchContent(
    productViewModel: ProductViewModel,
    navController: NavController,
    materialsViewModel: MaterialsViewModel,
    drawerState: CustomDrawerState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDrawerClick: (CustomDrawerState) -> Unit,
    modifier: Modifier
) {
    var searchTextFieldValue by remember { mutableStateOf(TextFieldValue()) }
    var showError by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val productsState = productViewModel.products.collectAsState()
    val products = productsState.value

    Scaffold(
        modifier = modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - RECHERCHE",
                onMenuClick = { onDrawerClick(drawerState.opposite()) },
                onSearchClick = { navController.navigate("search") }
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(16.dp).clickable(enabled = drawerState == CustomDrawerState.Closed) { }) {
                    Spacer(modifier = Modifier.height(55.dp))
                    OutlinedTextField(
                        value = searchTextFieldValue,
                        onValueChange = {
                            searchTextFieldValue = it
                            onSearchQueryChange(it.text)
                            showError = it.text.length < 3
                            if (!showError) {
                                coroutineScope.launch {
                                    productViewModel.getProducts(search = it.text)
                                }
                            }
                        },
                        label = { Text("Recherche") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true,
                        isError = showError,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        )
                    )
                    if (showError) {
                        Text(
                            text = "La recherche doit contenir au moins 3 caractères",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        Text(text = "Nombre de résultats (${products.size})", modifier = Modifier.padding(16.dp))
                        ProductList(
                            products = products,
                            productViewModel = productViewModel,
                            categoryId = null,
                            navController = navController,
                            materialsViewModel = materialsViewModel,
                            searchQuery = searchQuery
                        )
                    }
                }
            }
        }
    )
}