package com.example.airneis_sdv_app.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import com.example.airneis_sdv_app.viewmodel.ProductViewModel
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.airneis_sdv_app.data.FilterState
import com.example.airneis_sdv_app.ui.theme.BlueAIRNEIS
import com.example.airneis_sdv_app.viewmodel.MaterialsViewModel


@SuppressLint("MutableCollectionMutableState")
@Composable
fun ProductsFilters(
    productViewModel: ProductViewModel,
    categoryId: Int?,
    materialsViewModel: MaterialsViewModel,
    initialMinPrice: String,
    initialMaxPrice: String,
    initialSelectedMaterials: List<String>,
    initialIsStockChecked: Boolean,
    sortOption: String,
    onCloseDialog: (FilterState) -> Unit
) {
    var minPrice by rememberSaveable { mutableStateOf(initialMinPrice) }
    var maxPrice by rememberSaveable { mutableStateOf(initialMaxPrice) }
    val selectedMaterials by rememberSaveable { mutableStateOf(initialSelectedMaterials.toMutableList()) }
    var isStockChecked by rememberSaveable { mutableStateOf(initialIsStockChecked) }

    Log.d(
        "Filters",
        "Before updating filters: MinPrice=$minPrice, MaxPrice=$maxPrice, SelectedMaterials=$selectedMaterials, IsStockChecked=$isStockChecked"
    )

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        materialsViewModel.loadMaterialsFromAPI(context)
    }
    val materials = materialsViewModel.materials.collectAsState()
    val CheckBlueAIRNEIS = CheckboxDefaults.colors(
        checkedColor = Color.Blue,
        uncheckedColor = Color.Gray,
        checkmarkColor = Color.White,
        disabledIndeterminateColor = Color.DarkGray
    )
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Prix :", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Min", fontSize = 12.sp)
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
                Text("Max", fontSize = 12.sp)
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

        Text(text = "Matériaux", fontSize = 12.sp)
        val rememberedMaterials = remember { selectedMaterials.toList() }
        LazyColumn {
            items(materials.value) { material ->
                var isChecked by remember { mutableStateOf(rememberedMaterials.contains(material.id.toString())) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            isChecked = checked
                            if (checked) {
                                selectedMaterials.add(material.id.toString())
                            } else {
                                selectedMaterials.remove(material.id.toString())
                            }
                        }
                    )
                    Text(text = material.name)
                }
            }
        }
        Text(text = "Stock", fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isStockChecked, onCheckedChange = { isStockChecked = it }, colors = CheckBlueAIRNEIS)
            Text(text = "En stock")
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
        Button(
            onClick = {
                Log.d(
                    "Filters",
                    "Applying filters: MinPrice=$minPrice, MaxPrice=$maxPrice, SelectedMaterials=$selectedMaterials, IsStockChecked=$isStockChecked"
                )
                productViewModel.getProducts(
                    minPrice = minPrice.toFloatOrNull(),
                    maxPrice = maxPrice.toFloatOrNull(),
                    sortOrder = sortOption,
                    order = null,
                    categoryId = categoryId,
                    materials = selectedMaterials.toList(),
                    stock = if (isStockChecked) "1" else "0"
                )
                onCloseDialog(
                    FilterState(
                        minPrice,
                        maxPrice,
                        selectedMaterials.toList(),
                        isStockChecked
                    )
                )
            },
            modifier = Modifier
                .height(36.dp)
                .width(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueAIRNEIS
            )
        ) {
            Text("Filtrer", fontSize = 10.sp)
        }
    }
}
}
