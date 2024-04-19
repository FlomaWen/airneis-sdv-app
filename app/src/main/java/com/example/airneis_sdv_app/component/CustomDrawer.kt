package com.example.airneis_sdv_app.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airneis_sdv_app.model.Categories
import com.example.airneis_sdv_app.model.NavigationItem


@Composable
fun CustomDrawer(
    selectedNavigationItem: NavigationItem,
    navController: NavController,
    onNavigationItemClick: (NavigationItem) -> Unit,
    onCloseClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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

        NavigationItem.entries.filter { it != NavigationItem.Settings }.forEach { navigationItem ->
            if (navigationItem == NavigationItem.Categories) {
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
                    Categories.entries.forEach { category ->
                        Text(
                            text = category.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("categoryScreen/${category.id}")
                                    expanded = false
                                }
                                .padding(start = 40.dp,)
                                .padding(8.dp)
                        )
                    }
                }
            } else {
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
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        NavigationItemView(
            navigationItem = NavigationItem.Settings,
            selected = NavigationItem.Settings == selectedNavigationItem,
            onClick = { onNavigationItemClick(NavigationItem.Settings) }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
