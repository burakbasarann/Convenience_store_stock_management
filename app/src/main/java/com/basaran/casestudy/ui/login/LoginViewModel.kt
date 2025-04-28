package com.basaran.casestudy.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.repository.user.UserRepository
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState>()
    val loginState: LiveData<UiState> = _loginState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = UiState.Error("Kullanıcı Adı ve Şifre Boş Bırakmayınız")
            return
        }

        _loginState.value = UiState.Loading

        viewModelScope.launch {
            val user = userRepository.loginUser(username, password)
            delay(1000)
            if (user != null) {
                UserManager.saveUserId(user.id)
                _loginState.value = UiState.Success
            } else {
                _loginState.value = UiState.Error("Kullanıcı veya Şifre Yanlış")
            }
        }
    }

    fun register(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = UiState.Error("Kullanıcı Adı ve Şifre Boş Bırakmayınız")
            return
        }

        _loginState.value = UiState.Loading

        viewModelScope.launch {
            val userId = userRepository.registerUser(username, password)
            delay(1000)
            if (userId == null) {
                _loginState.value = UiState.Error("Bu kullanıcı adı zaten alınmış")
                return@launch
            }
            UserManager.saveUserId(userId)
            _loginState.value = UiState.Success
        }
    }
}
