package com.example.airneis_sdv_app.view

import android.util.Log
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.airneis_sdv_app.component.GlobalApp.AppTopBar
import com.example.airneis_sdv_app.component.GlobalApp.Drawer.CustomDrawer
import com.example.airneis_sdv_app.component.Product.DetailProduct
import com.example.airneis_sdv_app.model.Drawer.CustomDrawerState
import com.example.airneis_sdv_app.model.Drawer.NavigationItem
import com.example.airneis_sdv_app.model.Drawer.isOpened
import com.example.airneis_sdv_app.model.Drawer.opposite
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import com.example.airneis_sdv_app.viewmodel.ProductViewModel
import isUserLoggedIn
import kotlin.math.roundToInt

@Composable
fun ProductDetailScreen(navController: NavHostController,
                        categoryViewModel: CategoryViewModel,
                        categoryId: Int,
                        productId: Int,
                        productViewModel: ProductViewModel
) {
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Cart) }
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

    val categoryState = categoryViewModel.categories.collectAsState()
    val categoryName = categoryState.value.find { it.id == categoryId }?.name ?: "Catégorie Inconnue"

    LaunchedEffect(productId) {
        productViewModel.getProductById(productId = productId)
    }

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

        DetailProductContent(
            modifier = Modifier
                .offset(x = animatedOffset)
                .scale(animatedScale),
            drawerState = drawerState,
            onDrawerClick = { drawerState = it },
            categoryName = categoryName,
            productViewModel = productViewModel,
            navController = navController
        )
    }
}

@Composable
fun DetailProductContent(
    modifier: Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    categoryName: String,
    productViewModel: ProductViewModel,
    navController: NavController
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
                onSearchClick = { navController.navigate("search") }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                val productState by productViewModel.product.collectAsState()
                Log.d("ProductState", productState.toString())
                DetailProduct(productState,navController)
            }
        }
    )
}