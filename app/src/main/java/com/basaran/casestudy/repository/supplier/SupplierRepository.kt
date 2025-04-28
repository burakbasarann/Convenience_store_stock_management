package com.basaran.casestudy.repository.supplier

import com.basaran.casestudy.data.model.Supplier
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers(userId: String): Flow<List<Supplier>>
    suspend fun getSupplierById(supplierId: Long, userId: String): Supplier?
    suspend fun insertSupplier(supplier: Supplier)
    suspend fun updateSupplier(supplier: Supplier)
    suspend fun deleteSupplier(supplier: Supplier)
    fun searchSuppliers(userId: String, query: String): Flow<List<Supplier>>
    suspend fun seedInitialData(userId: String)
}
