package com.example.airneis_sdv_app.component.Account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airneis_sdv_app.component.GlobalApp.AppTopBar
import com.example.airneis_sdv_app.component.GlobalApp.ButtonStyled
import com.example.airneis_sdv_app.model.Drawer.CustomDrawerState
import com.example.airneis_sdv_app.model.User
import com.example.airneis_sdv_app.model.Drawer.opposite
import com.example.airneis_sdv_app.viewmodel.Account.AccountViewModel
import com.example.airneis_sdv_app.viewmodel.Account.AddressViewModel
import com.example.airneis_sdv_app.viewmodel.Account.PaymentMethodsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AccountContent(
    modifier: Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    navController: NavController,
    user: User?,
    accountViewModel: AccountViewModel,
    addressViewModel: AddressViewModel,
    paymentMethodsViewModel: PaymentMethodsViewModel
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var isPasswordDialogOpen by remember { mutableStateOf(false) }
    var isAddAddressDialogOpen by remember { mutableStateOf(false) }
    var fieldToEdit by remember { mutableStateOf("") }
    var inputValue by remember { mutableStateOf(TextFieldValue("")) }
    var oldPassword by remember { mutableStateOf(TextFieldValue("")) }
    var newPassword by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    val addresses by addressViewModel.addresses.collectAsState()
    val errorMessageAdresses by addressViewModel.errorMessage.collectAsState()
    val loadingAddresses by addressViewModel.loading.collectAsState()
    val paymentMethods by paymentMethodsViewModel.paymentMethods.collectAsState()
    val errorMessagePaymentMethods by paymentMethodsViewModel.errorMessage.collectAsState()
    val loadingPaymentMethods by paymentMethodsViewModel.loading.collectAsState()


    Scaffold(
        modifier = modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        topBar = {
            AppTopBar(
                title = "ÀIRNEIS - MON COMPTE",
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(text = "Informations Personnelles", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (user != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Nom : ${user.name}")
                            IconButton(onClick = {
                                fieldToEdit = "Nom"
                                inputValue = TextFieldValue(user.name)
                                isDialogOpen = true
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Modifier")
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Email: ${user.email}")
                            IconButton(onClick = {
                                fieldToEdit = "Email"
                                inputValue = TextFieldValue(user.email)
                                isDialogOpen = true
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Modifier")
                            }
                        }
                        val formatted = formatDate(user.createdAt)
                        Text(text = "Date d'inscription : $formatted")

                        Spacer(modifier = Modifier.height(16.dp))

                        ButtonStyled(onClick = {
                            isPasswordDialogOpen = true
                        }, text = "Changer de mot de passe")

                        Spacer(modifier = Modifier.height(16.dp))
                        // Section adresses
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Adresses", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = { isAddAddressDialogOpen = true }) {
                                Text("Nouvelle adresse")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        if (errorMessageAdresses != null) {
                            Text(text = "Erreur: $errorMessageAdresses", color = MaterialTheme.colorScheme.error)
                        } else if (loadingAddresses) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else if (addresses.isEmpty()) {
                            Text(text = "Aucune adresse trouvée")
                        } else {
                            LazyColumn {
                                items(addresses) { address ->
                                    AddressCard(
                                        address = address,
                                        onEditClick = {
                                            // TODO: Handle edit address
                                        },
                                        onDeleteClick = {
                                            Log.d("AccountContent", "Deleting address with ID: ${address.id}")
                                            try {
                                                addressViewModel.deleteAddress(context, address.id) {
                                                    Log.d("AccountContent", "Address deleted, reloading addresses")
                                                    addressViewModel.loadAddressesFromAPI(context)
                                                }
                                            } catch (e: Exception) {
                                                Log.e("AccountContent", "Error deleting address", e)
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        // Section méthodes de paiement
                        Text(text = "Méthodes de paiement", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (errorMessagePaymentMethods != null) {
                            Text(text = "Erreur: $errorMessagePaymentMethods", color = MaterialTheme.colorScheme.error)
                        } else if (loadingPaymentMethods) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else if (paymentMethods.isEmpty()) {
                            Text(text = "Aucune méthode de paiement trouvée")
                        } else {
                            LazyColumn {
                                items(paymentMethods) { paymentMethod ->
                                    PaymentMethodCard(
                                        paymentMethod = paymentMethod,
                                        onEditClick = {
                                            // TODO: Handle edit payment method
                                        },
                                        onDeleteClick = {
                                            Log.d("AccountContent", "Deleting payment method with ID: ${paymentMethod.id}")
                                            // TODO: Handle delete payment method
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    if (isDialogOpen) {
        EditFieldDialog(
            fieldToEdit = fieldToEdit,
            inputValue = inputValue,
            onValueChange = { inputValue = it },
            onDismiss = { isDialogOpen = false },
            onSave = {
                Log.d("AccountContent", "Saving changes for field: $fieldToEdit with value: ${inputValue.text}")
                user?.let {
                    when (fieldToEdit) {
                        "Nom" -> accountViewModel.updateUser(
                            context,
                            name = inputValue.text,
                            newEmail = it.email,
                            currentEmail = it.email,
                            defaultBillingAddressId = it.defaultBillingAddress,
                            defaultShippingAddressId = it.defaultShippingAddress
                        ) {
                            accountViewModel.loadUserFromAPI(context)
                        }
                        "Email" -> accountViewModel.updateUser(
                            context,
                            name = it.name,
                            newEmail = inputValue.text,
                            currentEmail = it.email,
                            defaultBillingAddressId = it.defaultBillingAddress,
                            defaultShippingAddressId = it.defaultShippingAddress
                        ) {
                            accountViewModel.loadUserFromAPI(context)
                        }
                    }
                    isDialogOpen = false
                } ?: run {
                    Log.e("AccountContent", "User object is null")
                }
            }
        )
    }

    if (isPasswordDialogOpen) {
        PasswordChangeDialog(
            oldPassword = oldPassword,
            newPassword = newPassword,
            onOldPasswordChange = { oldPassword = it },
            onNewPasswordChange = { newPassword = it },
            onDismiss = { isPasswordDialogOpen = false },
            onSave = {
                Log.d("AccountContent", "Changing password from: ${oldPassword.text} to: ${newPassword.text}")
                accountViewModel.changePassword(context, oldPassword.text, newPassword.text) {
                    isPasswordDialogOpen = false
                }
            }
        )
    }

    if (isAddAddressDialogOpen) {
        AddAddressDialog(
            onDismiss = { isAddAddressDialogOpen = false },
            onSave = { address ->
                Log.d("AccountContent", "Adding new address: $address")
                addressViewModel.addAddress(context, address) {
                    isAddAddressDialogOpen = false
                    addressViewModel.loadAddressesFromAPI(context)
                }
            }
        )
    }
}

fun formatDate(inputDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDateTime.parse(inputDate, inputFormatter)
    return date.format(outputFormatter)
}