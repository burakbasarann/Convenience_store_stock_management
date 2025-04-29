package com.basaran.casestudy

import com.basaran.casestudy.data.local.dao.SupplierDao
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.repository.supplier.SupplierRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SupplierRepositoryImplTest {

    private lateinit var supplierDao: SupplierDao
    private lateinit var repository: SupplierRepositoryImpl
    private val userId = "test_user_id"

    @Before
    fun setup() {
        supplierDao = mockk()
        repository = SupplierRepositoryImpl(supplierDao)
    }

    @Test
    fun `getAllSuppliers returns flow of suppliers`() = runBlocking {
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
        coEvery { supplierDao.getAllSuppliers(userId) } returns flowOf(suppliers)
        val result = repository.getAllSuppliers(userId).first()
        assertEquals(suppliers, result)
        coVerify { supplierDao.getAllSuppliers(userId) }
    }

    @Test
    fun `getSupplierById returns supplier when exists`() = runBlocking {
        val supplier = Supplier(
            id = 1,
            userId = userId,
            name = "Test Supplier",
            contactPerson = "John Doe",
            phone = "1234567890",
            email = "test@example.com",
            address = "Test Address"
        )
        coEvery { supplierDao.getSupplierById(1, userId) } returns supplier
        val result = repository.getSupplierById(1, userId)
        assertEquals(supplier, result)
        coVerify { supplierDao.getSupplierById(1, userId) }
    }

    @Test
    fun `insertSupplier calls dao insertSupplier`() = runBlocking {
        val supplier = Supplier(
            id = 1,
            userId = userId,
            name = "Test Supplier",
            contactPerson = "John Doe",
            phone = "1234567890",
            email = "test@example.com",
            address = "Test Address"
        )
        coEvery { supplierDao.insertSupplier(supplier) } returns 1L
        repository.insertSupplier(supplier)
        coVerify { supplierDao.insertSupplier(supplier) }
    }

    @Test
    fun `updateSupplier calls dao updateSupplier`() = runBlocking {
        val supplier = Supplier(
            id = 1,
            userId = userId,
            name = "Test Supplier",
            contactPerson = "John Doe",
            phone = "1234567890",
            email = "test@example.com",
            address = "Test Address"
        )
        coEvery { supplierDao.updateSupplier(supplier) } returns Unit
        repository.updateSupplier(supplier)
        coVerify { supplierDao.updateSupplier(supplier) }
    }

    @Test
    fun `deleteSupplier calls dao deleteSupplier`() = runBlocking {
        val supplier = Supplier(
            id = 1,
            userId = userId,
            name = "Test Supplier",
            contactPerson = "John Doe",
            phone = "1234567890",
            email = "test@example.com",
            address = "Test Address"
        )
        coEvery { supplierDao.deleteSupplier(supplier) } returns Unit
        repository.deleteSupplier(supplier)
        coVerify { supplierDao.deleteSupplier(supplier) }
    }
} 