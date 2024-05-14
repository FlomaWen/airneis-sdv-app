package com.example.airneis_sdv_app.viewmodel.SignUp

sealed class SignupUIEvent{

    data class NameChanged(val name:String) : SignupUIEvent()
    data class EmailChanged(val email:String): SignupUIEvent()
    data class PasswordChanged(val password: String) : SignupUIEvent()

    data class PrivacyPolicyCheckBoxClicked(val status:Boolean) : SignupUIEvent()

    data object RegisterButtonClicked : SignupUIEvent()
}