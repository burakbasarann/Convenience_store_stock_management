package com.basaran.casestudy.utils

sealed class UiState {
    data object Success : UiState()
    data object Loading : UiState()
    data object Idle : UiState()
    data class Error(val message: String) : UiState()
}