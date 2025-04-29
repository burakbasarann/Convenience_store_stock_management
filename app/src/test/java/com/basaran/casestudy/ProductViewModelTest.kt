package com.basaran.casestudy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.repository.product.ProductRepository
import com.basaran.casestudy.ui.products.ProductsViewModel
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
class ProductViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var productRepository: ProductRepository
    private lateinit var viewModel: ProductsViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val userId = "test_user_id"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(UserManager)
        every { UserManager.getUserId() } returns userId

        productRepository = mockk()
        coEvery { productRepository.getAllProducts(userId) } returns flowOf(emptyList())
        coEvery { productRepository.seedInitialData(userId) } returns Unit
        viewModel = ProductsViewModel(productRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads products and updates state`() = runTest {
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
        coEvery { productRepository.seedInitialData(userId) } returns Unit

        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.products.value).isEqualTo(products)
        assertThat(viewModel.filteredProducts.value).isEqualTo(products)
        assertThat(viewModel.uiState.value).isInstanceOf(UiState.Success::class.java)
    }

    @Test
    fun `init with error updates state to Error`() = runTest {
        coEvery { productRepository.getAllProducts(userId) } throws Exception("Test error")
        coEvery { productRepository.seedInitialData(userId) } returns Unit

        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isInstanceOf(UiState.Error::class.java)
        assertThat((viewModel.uiState.value as UiState.Error).message).isEqualTo("Test error")
    }

    @Test
    fun `filterProducts filters products by name`() = runTest {
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
            ),
            Product(
                id = 2,
                userId = userId,
                name = "Another Product",
                description = "Another Description",
                price = 20.0,
                category = "Another Category",
                barcode = "654321",
                currentStock = 20,
                minStock = 10
            )
        )
        coEvery { productRepository.getAllProducts(userId) } returns flowOf(products)
        coEvery { productRepository.seedInitialData(userId) } returns Unit

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.filterProducts("Test")

        assertThat(viewModel.filteredProducts.value).hasSize(1)
        assertThat(viewModel.filteredProducts.value?.first()?.name).isEqualTo("Test Product")
    }

    @Test
    fun `sortProductsAscending sorts products by name`() = runTest {
        val products = listOf(
            Product(
                id = 2,
                userId = userId,
                name = "Z Product",
                description = "Description",
                price = 20.0,
                category = "Category",
                barcode = "654321",
                currentStock = 20,
                minStock = 10
            ),
            Product(
                id = 1,
                userId = userId,
                name = "A Product",
                description = "Description",
                price = 10.0,
                category = "Category",
                barcode = "123456",
                currentStock = 10,
                minStock = 5
            )
        )
        coEvery { productRepository.getAllProducts(userId) } returns flowOf(products)
        coEvery { productRepository.seedInitialData(userId) } returns Unit

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.sortProductsAscending()

        assertThat(viewModel.filteredProducts.value?.first()?.name).isEqualTo("A Product")
    }

    @Test
    fun `sortProductsDescending sorts products by name`() = runTest {
        val products = listOf(
            Product(
                id = 1,
                userId = userId,
                name = "A Product",
                description = "Description",
                price = 10.0,
                category = "Category",
                barcode = "123456",
                currentStock = 10,
                minStock = 5
            ),
            Product(
                id = 2,
                userId = userId,
                name = "Z Product",
                description = "Description",
                price = 20.0,
                category = "Category",
                barcode = "654321",
                currentStock = 20,
                minStock = 10
            )
        )
        coEvery { productRepository.getAllProducts(userId) } returns flowOf(products)
        coEvery { productRepository.seedInitialData(userId) } returns Unit

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.sortProductsDescending()

        assertThat(viewModel.filteredProducts.value?.first()?.name).isEqualTo("Z Product")
    }
} 