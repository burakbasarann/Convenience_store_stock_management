package com.basaran.casestudy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.basaran.casestudy.data.model.User
import com.basaran.casestudy.repository.user.UserRepository
import com.basaran.casestudy.ui.login.LoginViewModel
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(UserManager)
        userRepository = mockk()
        viewModel = LoginViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with valid credentials updates state to Success`() = runTest {
        val username = "test@example.com"
        val password = "password123"
        val user = User(id = "test_user_id", username = username, password = password)
        coEvery { userRepository.loginUser(username, password) } returns user
        every { UserManager.saveUserId(any()) } returns Unit
        viewModel.login(username, password)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.loginState.value).isInstanceOf(UiState.Success::class.java)
    }

    @Test
    fun `login with invalid credentials updates state to Error`() = runTest {
        val username = "test@example.com"
        val password = "wrong_password"
        coEvery { userRepository.loginUser(username, password) } returns null
        viewModel.login(username, password)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.loginState.value).isInstanceOf(UiState.Error::class.java)
        assertThat((viewModel.loginState.value as UiState.Error).message).isEqualTo("Kullanıcı veya Şifre Yanlış")
    }

    @Test
    fun `login with empty credentials updates state to Error`() = runTest {
        val username = ""
        val password = ""
        viewModel.login(username, password)
        assertThat(viewModel.loginState.value).isInstanceOf(UiState.Error::class.java)
        assertThat((viewModel.loginState.value as UiState.Error).message).isEqualTo("Kullanıcı Adı ve Şifre Boş Bırakmayınız")
    }

    @Test
    fun `register with valid credentials updates state to Success`() = runTest {
        val username = "test@example.com"
        val password = "password123"
        val userId = "test_user_id"
        coEvery { userRepository.registerUser(username, password) } returns userId
        every { UserManager.saveUserId(any()) } returns Unit
        viewModel.register(username, password)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.loginState.value).isInstanceOf(UiState.Success::class.java)
    }

    @Test
    fun `register with existing username updates state to Error`() = runTest {
        val username = "existing@example.com"
        val password = "password123"
        coEvery { userRepository.registerUser(username, password) } returns null
        viewModel.register(username, password)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.loginState.value).isInstanceOf(UiState.Error::class.java)
        assertThat((viewModel.loginState.value as UiState.Error).message).isEqualTo("Bu kullanıcı adı zaten alınmış")
    }

    @Test
    fun `register with empty credentials updates state to Error`() = runTest {
        val username = ""
        val password = ""
        viewModel.register(username, password)
        assertThat(viewModel.loginState.value).isInstanceOf(UiState.Error::class.java)
        assertThat((viewModel.loginState.value as UiState.Error).message).isEqualTo("Kullanıcı Adı ve Şifre Boş Bırakmayınız")
    }
} 