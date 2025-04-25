package com.basaran.casestudy.utils

sealed class LoginState {
    data object Success : LoginState()
    data object Loading : LoginState()
    data class Error(val message: String) : LoginState()
}