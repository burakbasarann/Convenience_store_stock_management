package com.basaran.casestudy.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.repository.product.ProductRepository
import com.basaran.casestudy.repository.transaction.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _lowStockProducts = MutableLiveData<List<Product>>()
    val lowStockProducts: LiveData<List<Product>> get() = _lowStockProducts

    private val _recentTransactions = MutableLiveData<List<Transaction>>()
    val recentTransactions: LiveData<List<Transaction>> get() = _recentTransactions

    init {
        getLowStockProducts()
        getRecentTransactions()
    }

    private fun getLowStockProducts() {
        viewModelScope.launch {
            productRepository.getLowStockProducts().collect { products ->
                _lowStockProducts.postValue(products)
            }
        }
    }

    private fun getRecentTransactions() {
        viewModelScope.launch {
            transactionRepository.getAllTransactions().collect { products ->
                _recentTransactions.postValue(products)
            }
        }
    }
}