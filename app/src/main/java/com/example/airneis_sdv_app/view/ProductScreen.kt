package com.example.airneis_sdv_app.view

import Product
import com.example.airneis_sdv_app.viewmodel.ProductViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.AppTopBar
import com.example.airneis_sdv_app.component.CustomDrawer
import com.example.airneis_sdv_app.component.ProductList
import com.example.airneis_sdv_app.model.CustomDrawerState
import com.example.airneis_sdv_app.model.NavigationItem
import com.example.airneis_sdv_app.model.isOpened
import com.example.airneis_sdv_app.model.opposite
import com.example.airneis_sdv_app.util.coloredShadow
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import com.example.airneis_sdv_app.viewmodel.MaterialsViewModel
import isUserLoggedIn
import kotlin.math.roundToInt

@Composable
fun ProductScreen(
    categoryId: Int,
    navController: NavController,
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    materialsViewModel: MaterialsViewModel
) {
    val categoryState = categoryViewModel.categories.collectAsState()
    val categoryName = categoryState.value.find { it.id == categoryId }?.name ?: "Catégorie Inconnue"

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


    LaunchedEffect(categoryId) {
        productViewModel.getProducts(categoryId)
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
        ProductContent(
            modifier = Modifier
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.1f,
                    shadowRadius = 50.dp
                ),
            drawerState = drawerState,
            onDrawerClick = { drawerState = it },
            categoryName = categoryName,
            products = products,
            categoryId = categoryId,
            productViewModel = productViewModel,
            navController = navController,
            materialsViewModel = materialsViewModel
        )
    }
}

@Composable
fun ProductContent(
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    categoryName: String,
    products: List<Product>,
    modifier: Modifier,
    categoryId:Int,
    productViewModel: ProductViewModel,
    navController: NavController,
    materialsViewModel: MaterialsViewModel
) {

    Scaffold(
        modifier = modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - $categoryName",
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
                    ProductList(products = products,categoryId = categoryId,productViewModel = productViewModel,navController = navController, materialsViewModel = materialsViewModel)
            }
        }
    )
}

