package com.example.airneis_sdv_app.view

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.GlobalApp.AppTopBar
import com.example.airneis_sdv_app.component.Caroussels.CarousselNoData
import com.example.airneis_sdv_app.component.CategoryView
import com.example.airneis_sdv_app.component.GlobalApp.Drawer.CustomDrawer
import com.example.airneis_sdv_app.model.Category
import com.example.airneis_sdv_app.model.Drawer.CustomDrawerState
import com.example.airneis_sdv_app.model.Drawer.NavigationItem
import com.example.airneis_sdv_app.model.Drawer.isOpened
import com.example.airneis_sdv_app.model.Drawer.opposite
import com.example.airneis_sdv_app.util.coloredShadow
import com.example.airneis_sdv_app.viewmodel.CategoryViewModel
import isUserLoggedIn
import kotlin.math.roundToInt

@Composable
fun MainScreen(navController: NavController, categoryViewModel: CategoryViewModel = viewModel()) {
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Home) }
    val categoriesState = categoryViewModel.categories.collectAsState()
    LaunchedEffect(true) {
        categoryViewModel.getCategories()
    }
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
            onCloseClick = { drawerState = CustomDrawerState.Closed },
            categories = categoriesState.value,
            isUserLoggedIn = isUserLoggedIn(LocalContext.current)
        )
        MainContent(
            navController = navController,
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
            categories = categoriesState.value
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    navController: NavController,
    categories: List<Category>
) {
    Scaffold(
        modifier = modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - ACCEUIL",
                onMenuClick = { onDrawerClick(drawerState.opposite()) },
                onSearchClick = { navController.navigate("search") }
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize())
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Spacer(modifier = Modifier.height(55.dp))
                    CarousselNoData(modifier = Modifier.fillMaxWidth())
                }
                items(categories) { category ->
                    CategoryView(category, navController)
                }
            }
        }
    )
}

