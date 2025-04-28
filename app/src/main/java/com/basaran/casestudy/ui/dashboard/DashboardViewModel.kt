package com.basaran.casestudy.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.repository.product.ProductRepository
import com.basaran.casestudy.repository.transaction.TransactionRepository
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _allProducts = MutableLiveData<List<Product>>(emptyList())
    val allProducts: LiveData<List<Product>> get() = _allProducts

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    init {
        getLowStockProducts()
        getRecentTransactions()

        viewModelScope.launch {
            try {
                _uiState.postValue(UiState.Loading)
                delay(300)
                productRepository.getAllProducts(UserManager.getUserId()).collect { allProducts ->
                    _allProducts.value = allProducts
                    _uiState.postValue(UiState.Success)
                }
            } catch (e: Exception) {
                _uiState.postValue(UiState.Error(e.localizedMessage ?: "Bir hata oluştu"))
            }
        }
    }

    private fun getLowStockProducts() {
        viewModelScope.launch {
            try {
                _uiState.postValue(UiState.Loading)
                delay(300)
                productRepository.getLowStockProducts(UserManager.getUserId()).collect { products ->
                    _lowStockProducts.postValue(products)
                    _uiState.postValue(UiState.Success)
                }
            } catch (e: Exception) {
                _uiState.postValue(UiState.Error(e.localizedMessage ?: "Bir hata oluştu"))
            }
        }
    }

    private fun getRecentTransactions() {
        viewModelScope.launch {
            try {
                _uiState.postValue(UiState.Loading)
                delay(300)
                transactionRepository.getAllTransactions(UserManager.getUserId()).collect { transactions ->
                    _recentTransactions.postValue(transactions)
                    _uiState.postValue(UiState.Success)
                }
            } catch (e: Exception) {
                _uiState.postValue(UiState.Error(e.localizedMessage ?: "Bir hata oluştu"))
            }
        }
    }
}
