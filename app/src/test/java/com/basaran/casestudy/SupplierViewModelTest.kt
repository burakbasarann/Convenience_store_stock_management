package com.basaran.casestudy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.repository.supplier.SupplierRepository
import com.basaran.casestudy.ui.suppliers.SuppliersViewModel
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
class SupplierViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var supplierRepository: SupplierRepository
    private lateinit var viewModel: SuppliersViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val userId = "test_user_id"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(UserManager)
        every { UserManager.getUserId() } returns userId

        supplierRepository = mockk()
        coEvery { supplierRepository.getAllSuppliers(userId) } returns flowOf(emptyList())
        coEvery { supplierRepository.seedInitialData(userId) } returns Unit
        viewModel = SuppliersViewModel(supplierRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads suppliers and updates state`() = runTest {
        val suppliers = listOf(
            Supplier(
                id = 1,
                userId = userId,
                name = "Test Supplier",
                contactPerson = "John Doe",
                phone = "1234567890",
                email = "test@example.com",
                address = "Test Address"
            )
        )
        coEvery { supplierRepository.getAllSuppliers(userId) } returns flowOf(suppliers)
        coEvery { supplierRepository.seedInitialData(userId) } returns Unit

        // Create a new ViewModel with the updated mocks
        viewModel = SuppliersViewModel(supplierRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.suppliers.value).isEqualTo(suppliers)
        assertThat(viewModel.filteredSuppliers.value).isEqualTo(suppliers)
    }

    @Test
    fun `filterSuppliers filters suppliers by name`() = runTest {
        val suppliers = listOf(
            Supplier(
                id = 1,
                userId = userId,
                name = "Test Supplier",
                contactPerson = "John Doe",
                phone = "1234567890",
                email = "test@example.com",
                address = "Test Address"
            ),
            Supplier(
                id = 2,
                userId = userId,
                name = "Another Supplier",
                contactPerson = "Jane Doe",
                phone = "0987654321",
                email = "another@example.com",
                address = "Another Address"
            )
        )
        coEvery { supplierRepository.getAllSuppliers(userId) } returns flowOf(suppliers)
        coEvery { supplierRepository.seedInitialData(userId) } returns Unit

        // Create a new ViewModel with the updated mocks
        viewModel = SuppliersViewModel(supplierRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.filterSuppliers("Test")

        assertThat(viewModel.filteredSuppliers.value).hasSize(1)
        assertThat(viewModel.filteredSuppliers.value?.first()?.name).isEqualTo("Test Supplier")
    }

    @Test
    fun `sortSuppliersAscending sorts suppliers by name`() = runTest {
        val suppliers = listOf(
            Supplier(
                id = 2,
                userId = userId,
                name = "Z Supplier",
                contactPerson = "Jane Doe",
                phone = "0987654321",
                email = "another@example.com",
                address = "Another Address"
            ),
            Supplier(
                id = 1,
                userId = userId,
                name = "A Supplier",
                contactPerson = "John Doe",
                phone = "1234567890",
                email = "test@example.com",
                address = "Test Address"
            )
        )
        coEvery { supplierRepository.getAllSuppliers(userId) } returns flowOf(suppliers)
        coEvery { supplierRepository.seedInitialData(userId) } returns Unit

        // Create a new ViewModel with the updated mocks
        viewModel = SuppliersViewModel(supplierRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.sortSuppliersAscending()

        assertThat(viewModel.filteredSuppliers.value?.first()?.name).isEqualTo("A Supplier")
    }

    @Test
    fun `sortSuppliersDescending sorts suppliers by name`() = runTest {
        val suppliers = listOf(
            Supplier(
                id = 1,
                userId = userId,
                name = "A Supplier",
                contactPerson = "John Doe",
                phone = "1234567890",
                email = "test@example.com",
                address = "Test Address"
            ),
            Supplier(
                id = 2,
                userId = userId,
                name = "Z Supplier",
                contactPerson = "Jane Doe",
                phone = "0987654321",
                email = "another@example.com",
                address = "Another Address"
            )
        )
        coEvery { supplierRepository.getAllSuppliers(userId) } returns flowOf(suppliers)
        coEvery { supplierRepository.seedInitialData(userId) } returns Unit

        // Create a new ViewModel with the updated mocks
        viewModel = SuppliersViewModel(supplierRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.sortSuppliersDescending()

        assertThat(viewModel.filteredSuppliers.value?.first()?.name).isEqualTo("Z Supplier")
    }
} 