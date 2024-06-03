package com.example.airneis_sdv_app.component.GlobalApp.Drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airneis_sdv_app.model.Category
import com.example.airneis_sdv_app.model.Drawer.NavigationItem
import com.example.airneis_sdv_app.viewmodel.LogoutViewModel

@Composable
fun CustomDrawer(
    selectedNavigationItem: NavigationItem,
    navController: NavController,
    onNavigationItemClick: (NavigationItem) -> Unit,
    onCloseClick: () -> Unit,
    categories: List<Category>,
    isUserLoggedIn: Boolean,
    logoutViewModel: LogoutViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val logoutSuccess by logoutViewModel.logoutSuccessful.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            navController.navigate("LoginScreen") {
                popUpTo("MainScreen") { inclusive = true }
            }
            logoutViewModel.resetLogoutState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(fraction = 0.6f)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        NavigationItem.entries.filter { it != NavigationItem.Logout }.forEach { navigationItem ->
            if (navigationItem == NavigationItem.Cart && !isUserLoggedIn) {
                // Ne rien faire si l'utilisateur n'est pas connecté et que l'élément est le panier
            } else if (navigationItem == NavigationItem.Categories) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = !expanded })
                        .padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = navigationItem.icon),
                        contentDescription = "Navigation Item Icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Catégories", modifier = Modifier.weight(1f).padding(8.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
                if (expanded) {
                    categories.forEach { category ->
                        Text(
                            text = category.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("productScreen/${category.id}")
                                    expanded = false
                                }
                                .padding(start = 40.dp)
                                .padding(8.dp)
                        )
                    }
                }
            } else if (navigationItem == NavigationItem.Account && !isUserLoggedIn) {
                // Ne rien faire si l'utilisateur n'est pas connecté et que l'élément est le compte
            } else if (navigationItem != NavigationItem.Profile || !isUserLoggedIn) {
                NavigationItemView(
                    navigationItem = navigationItem,
                    selected = navigationItem == selectedNavigationItem,
                    onClick = {
                        onNavigationItemClick(navigationItem)
                        navController.navigate(navigationItem.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isUserLoggedIn) {
            NavigationItemView(
                navigationItem = NavigationItem.Logout,
                selected = NavigationItem.Logout == selectedNavigationItem,
                onClick = {
                    logoutViewModel.performLogout(context)
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
