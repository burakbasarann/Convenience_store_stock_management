package com.basaran.casestudy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.data.model.TransactionType
import com.basaran.casestudy.repository.product.ProductRepository
import com.basaran.casestudy.repository.transaction.TransactionRepository
import com.basaran.casestudy.ui.transactions.TransactionViewModel
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
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
class TransactionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var transactionRepository: TransactionRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var viewModel: TransactionViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val userId = "test_user_id"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(UserManager)
        every { UserManager.getUserId() } returns userId

        transactionRepository = mockk()
        productRepository = mockk()
        viewModel = TransactionViewModel(transactionRepository, productRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllTransactions updates transactions LiveData`() = runTest {
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
        coEvery { transactionRepository.getAllTransactions(userId) } returns flowOf(transactions)
        viewModel.getAllTransactions()
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.transactions.value).isEqualTo(transactions)
    }

    @Test
    fun `getAllProducts updates products LiveData`() = runTest {
        val products = listOf(
            Product(
                id = 1,
                userId = userId,
                name = "Test Product",
                description = "Test Description",
                price = 10.0,
                category = "Test Category",
                barcode = "123456",
                currentStock = 10,
                minStock = 5
            )
        )
        coEvery { productRepository.getAllProducts(userId) } returns flowOf(products)
        viewModel.getAllProducts()
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.products.value).isEqualTo(products)
    }

    @Test
    fun `addTransaction updates state to Success`() = runTest {
        val transaction = Transaction(
            id = 1,
            userId = userId,
            date = System.currentTimeMillis(),
            type = TransactionType.SALE,
            productId = 1,
            quantity = 5,
            notes = "Test note"
        )
        coEvery { transactionRepository.insertTransaction(transaction) } returns Unit
        coEvery { transactionRepository.getAllTransactions(userId) } returns flowOf(listOf(transaction))
        viewModel.addTransaction(transaction)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value).isInstanceOf(UiState.Success::class.java)
    }

    @Test
    fun `updateProductStock updates product stock`() = runTest {
        val product = Product(
            id = 1,
            userId = userId,
            name = "Test Product",
            description = "Test Description",
            price = 10.0,
            category = "Test Category",
            barcode = "123456",
            currentStock = 10,
            minStock = 5
        )
        coEvery { productRepository.getProductById(1, userId) } returns product
        coEvery { productRepository.updateProduct(any()) } returns Unit
        viewModel.updateProductStock(1, -5)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { productRepository.updateProduct(match { it.currentStock == 5 }) }
    }

    @Test
    fun `addTransaction with error updates state to Error`() = runTest {
        val transaction = Transaction(
            id = 1,
            userId = userId,
            date = System.currentTimeMillis(),
            type = TransactionType.SALE,
            productId = 1,
            quantity = 5,
            notes = "Test note"
        )
        coEvery { transactionRepository.insertTransaction(transaction) } throws Exception("Test error")
        viewModel.addTransaction(transaction)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value).isInstanceOf(UiState.Error::class.java)
        assertThat((viewModel.state.value as UiState.Error).message).isEqualTo("Test error")
    }
} 