package com.example.airneis_sdv_app.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.AppTopBar
import com.example.airneis_sdv_app.component.CustomDrawer
import com.example.airneis_sdv_app.component.ProductList
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.NavigationItem
import com.example.airneis_sdv_app.model.isOpened
import com.example.airneis_sdv_app.model.opposite
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import com.example.airneis_sdv_app.viewmodel.MaterialsViewModel
import com.example.airneis_sdv_app.viewmodel.ProductViewModel
import isUserLoggedIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SearchScreen(categoryViewModel: CategoryViewModel, productViewModel: ProductViewModel,navController: NavController,materialsViewModel: MaterialsViewModel) {
    val categoryState = categoryViewModel.categories.collectAsState()

    /* Drawer */
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Categories) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp,
        label = "Animated Offset"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
        label = "Animated Scale"
    )
    BackHandler(enabled = drawerState.isOpened()) {
        drawerState = CustomDrawerState.Closed
    }

    val products = productViewModel.products.collectAsState().value

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        CustomDrawer(
            selectedNavigationItem = selectedNavigationItem,
            navController = navController,
            onNavigationItemClick = {
                selectedNavigationItem = it
            },
            onCloseClick = { drawerState = CustomDrawerState.Closed},
            categories = categoryState.value,
            isUserLoggedIn = isUserLoggedIn(LocalContext.current)
        )
        
        SearchContent(
            productViewModel = productViewModel,
            navController = navController,
            materialsViewModel = materialsViewModel,
            drawerState = drawerState,
            onDrawerClick = { drawerState = it }
        )

    }
}

@Composable
fun SearchContent(productViewModel: ProductViewModel,
                  navController: NavController,
                  materialsViewModel: MaterialsViewModel,
                  drawerState: CustomDrawerState,
                  onDrawerClick: (CustomDrawerState) -> Unit) {
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - RECHERCHE",
                onMenuClick = { onDrawerClick(drawerState.opposite()) },
                onSearchClick = { /* TODO */ }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            coroutineScope.launch {
                                productViewModel.getProducts(search = it.text)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                            ) {
                                innerTextField()
                            }
                        }
                    )
                    ProductListContent(productViewModel = productViewModel,navController = navController, materialsViewModel = materialsViewModel)
                }
            }
        }
    )
}

@Composable
fun ProductListContent(productViewModel: ProductViewModel,navController: NavController, materialsViewModel: MaterialsViewModel) {
    val productsState = productViewModel.products.collectAsState()

    val products = productsState.value

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Liste des produits (${products.size})", modifier = Modifier.padding(16.dp))

        ProductList(products = products, productViewModel = productViewModel, categoryId = null, navController = navController, materialsViewModel = materialsViewModel)
    }
}