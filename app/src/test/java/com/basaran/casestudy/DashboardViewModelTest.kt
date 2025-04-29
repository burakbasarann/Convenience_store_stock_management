package com.basaran.casestudy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.data.model.TransactionType
import com.basaran.casestudy.repository.product.ProductRepository
import com.basaran.casestudy.repository.transaction.TransactionRepository
import com.basaran.casestudy.ui.dashboard.DashboardViewModel
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var productRepository: ProductRepository
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var viewModel: DashboardViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val userId = "test_user_id"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(UserManager)
        every { UserManager.getUserId() } returns userId

        productRepository = mockk()
        transactionRepository = mockk()
        viewModel = DashboardViewModel(productRepository, transactionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads low stock products and recent transactions`() = runTest {
        val products = listOf(
            Product(
                id = 1,
                userId = userId,
                name = "Test Product",
                description = "Test Description",
                price = 10.0,
                category = "Test Category",
                barcode = "123456",
                currentStock = 2,
                minStock = 5
            )
        )
        val transactions = listOf(
            Transaction(
                id = 1,
                userId = userId,
                date = System.currentTimeMillis(),
                type = TransactionType.SALE,
                productId = 1,
                quantity = 5,
                notes = "Test note"
            )
        )
        coEvery { productRepository.getLowStockProducts(userId) } returns flowOf(products)
        coEvery { transactionRepository.getAllTransactions(userId) } returns flowOf(transactions)
        coEvery { productRepository.getAllProducts(userId) } returns flowOf(products)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.lowStockProducts.value).isEqualTo(products)
        assertThat(viewModel.recentTransactions.value).isEqualTo(transactions)
        assertThat(viewModel.allProducts.value).isEqualTo(products)
    }

    @Test
    fun `init with error updates uiState to Error`() = runTest {
        coEvery { productRepository.getLowStockProducts(userId) } throws Exception("Test error")
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value).isInstanceOf(UiState.Error::class.java)
        assertThat((viewModel.uiState.value as UiState.Error).message).isEqualTo("Test error")
    }
} 