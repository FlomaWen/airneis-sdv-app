package com.example.airneis_sdv_app.component.Account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.airneis_sdv_app.component.GlobalApp.ButtonStyled
import com.example.airneis_sdv_app.component.GlobalApp.TextInputStyled
import com.example.airneis_sdv_app.model.AddressAdd

@Composable
fun AddAddressDialog(onDismiss: () -> Unit, onSave: (AddressAdd) -> Unit) {
    var label by remember { mutableStateOf(TextFieldValue("")) }
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var address1 by remember { mutableStateOf(TextFieldValue("")) }
    var address2 by remember { mutableStateOf(TextFieldValue("")) }
    var city by remember { mutableStateOf(TextFieldValue("")) }
    var region by remember { mutableStateOf(TextFieldValue("")) }
    var postalCode by remember { mutableStateOf(TextFieldValue("")) }
    var country by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var type by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter une nouvelle adresse") },
        text = {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                item {
                    TextInputStyled(
                        value = label,
                        onValueChange = { label = it },
                        label = { Text("Label") }
                    )
                }
                item {
                    TextInputStyled(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("Prénom") }
                    )
                }
                item {
                    TextInputStyled(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Nom") }
                    )
                }
                item {
                    TextInputStyled(
                        value = address1,
                        onValueChange = { address1 = it },
                        label = { Text("Adresse 1") }
                    )
                }
                item {
                    TextInputStyled(
                        value = address2,
                        onValueChange = { address2 = it },
                        label = { Text("Adresse 2") }
                    )
                }
                item {
                    TextInputStyled(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Ville") }
                    )
                }
                item {
                    TextInputStyled(
                        value = region,
                        onValueChange = { region = it },
                        label = { Text("Région") }
                    )
                }
                item {
                    TextInputStyled(
                        value = postalCode,
                        onValueChange = { postalCode = it },
                        label = { Text("Code postal") }
                    )
                }
                item {
                    TextInputStyled(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Pays") }
                    )
                }
                item {
                    TextInputStyled(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Téléphone") }
                    )
                }
                item {
                    TextInputStyled(
                        value = type,
                        onValueChange = { type = it },
                        label = { Text("Type") }
                    )
                }
            }
        },
        confirmButton = {
            ButtonStyled(onClick = {
                val newAddress = AddressAdd(
                    label = label.text,
                    firstName = firstName.text,
                    lastName = lastName.text,
                    address1 = address1.text,
                    address2 = address2.text,
                    city = city.text,
                    region = region.text,
                    postalCode = postalCode.text,
                    country = country.text,
                    phone = phone.text,
                    type = type.text
                )
                onSave(newAddress)
            }, text = "Ajouter")
        },
        dismissButton = {
            ButtonStyled(onClick = onDismiss, text = "Annuler")
        }
    )
}
