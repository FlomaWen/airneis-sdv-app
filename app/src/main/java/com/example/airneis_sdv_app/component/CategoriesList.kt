package com.example.airneis_sdv_app.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airneis_sdv_app.model.Categories


@Composable
fun CategoriesList(categories: List<Categories>, navController: NavController) {
    LazyColumn {
        items(categories) { category ->
            Log.d("CategoryDebug", "Loading category: ${category.title}")
            CategoryView(category, navController)
        }
    }
}
@Composable
fun CategoryView(category: Categories,navController: NavController) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
        .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(8.dp).align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f, true))
            IconButton(
                onClick = { navController.navigate("productScreen/${category.title}") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Go Icon",
                    tint = Color.Gray
                )
            }
        }
        Image(
            painter = painterResource(id = category.imageIDCat),
            contentDescription = category.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}

