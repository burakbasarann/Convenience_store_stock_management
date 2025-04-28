package com.basaran.casestudy.di

import com.basaran.casestudy.repository.product.ProductRepository
import com.basaran.casestudy.repository.product.ProductRepositoryImpl
import com.basaran.casestudy.repository.supplier.SupplierRepository
import com.basaran.casestudy.repository.supplier.SupplierRepositoryImpl
import com.basaran.casestudy.repository.transaction.TransactionRepository
import com.basaran.casestudy.repository.transaction.TransactionRepositoryImpl
import com.basaran.casestudy.repository.user.UserRepository
import com.basaran.casestudy.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindSupplierRepository(
        supplierRepositoryImpl: SupplierRepositoryImpl
    ): SupplierRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}

