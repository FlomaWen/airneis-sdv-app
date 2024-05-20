package com.example.airneis_sdv_app.component

import androidx.compose.foundation.border
import com.example.airneis_sdv_app.viewmodel.ProductViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.airneis_sdv_app.viewmodel.MaterialsViewModel

@Composable
fun ProductsFilters(productViewModel: ProductViewModel, categoryId: Int,materialsViewModel: MaterialsViewModel,initialMinPrice: String, initialMaxPrice: String, sortOption: String) {

    var minPrice by remember { mutableStateOf(initialMinPrice) }
    var maxPrice by remember { mutableStateOf(initialMaxPrice) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        materialsViewModel.loadMaterialsFromAPI(context)
    }
    val materials = materialsViewModel.materials.collectAsState()

Column(modifier = Modifier.padding(16.dp),) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Spacer(modifier=Modifier.height(10.dp))
            Text("Prix :", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("Min",fontSize = 12.sp)
            BasicTextField(
                value = minPrice,
                onValueChange = { minPrice = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .height(20.dp)
                    .width(80.dp)
                    .border(1.dp, Color.Black)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("Max",fontSize = 12.sp)
            BasicTextField(
                value = maxPrice,
                onValueChange = { maxPrice = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .height(20.dp)
                    .width(80.dp)
                    .border(1.dp, Color.Black)
            )
        }

    }

    Text(text="Matériaux",fontSize = 12.sp)
    LazyColumn {
        items(materials.value) { material ->
            var isChecked by remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text(text = material.name)
            }
        }
    }
    Text(text="Stock",fontSize = 12.sp)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = false, onCheckedChange = {  })
        Text(text = "En stock")
    }
    Button(
        onClick = {
            productViewModel.getProducts(
                minPrice = minPrice.toFloatOrNull(),
                maxPrice = maxPrice.toFloatOrNull(),
                sortOrder = sortOption,
                order = null,
                categoryId = categoryId
            )
        },
        modifier = Modifier
            .height(36.dp)
            .width(100.dp)
    ) {
        Text("Filtrer", fontSize = 10.sp)
    }
}

}




