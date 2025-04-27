package com.basaran.casestudy.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _loginState = MutableLiveData<UiState>()
    val loginState: LiveData<UiState> = _loginState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = UiState.Error("Kullanıcı Adı ve Şifre Boş Bırakmayınız")
            return
        }

        _loginState.value = UiState.Loading

        viewModelScope.launch {
            delay(1000)
            if (username == "1" && password == "1") {
                _loginState.value = UiState.Success
            } else {
                _loginState.value = UiState.Error("Kullanıcı veya Şifre Yanlış")
            }
        }
    }
}