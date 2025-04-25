package com.basaran.casestudy.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.utils.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Kullanıcı Adı ve Şifre Boş Bırakmayınız")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            if (username == "admin" && password == "admin123") {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Kullanıcı veya Şifre Yanlış")
            }
        }
    }
}