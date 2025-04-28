package com.basaran.casestudy.repository.supplier

import com.basaran.casestudy.data.model.Supplier
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers() : Flow<List<Supplier>>
    suspend fun getSupplierById(supplierId: Long) : Supplier?
    suspend fun insertSupplier(supplier: Supplier)
    suspend fun updateSupplier(supplier: Supplier)
    suspend fun deleteSupplier(supplier: Supplier)
    fun searchSuppliers(query: String): Flow<List<Supplier>>
    suspend fun seedInitialData()
}