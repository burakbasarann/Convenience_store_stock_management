package com.basaran.casestudy.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.repository.product.ProductRepository
import com.basaran.casestudy.repository.transaction.TransactionRepository
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    fun getAllTransactions() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            transactionRepository.getAllTransactions(UserManager.getUserId()).collect { transactionList ->
                _transactions.postValue(transactionList)
                _state.value = UiState.Success
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts(UserManager.getUserId()).collect { productList ->
                _products.postValue(productList)
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                transactionRepository.insertTransaction(transaction)
                getAllTransactions()
            } catch (e: Exception) {
                _state.value = UiState.Error(e.localizedMessage ?: "Error adding transaction")
            }
        }
    }

    fun updateProductStock(productId: Long, quantityChange: Int) {
        viewModelScope.launch {
            val product = productRepository.getProductById(productId, UserManager.getUserId())
            product?.let {
                val updatedStock = it.currentStock + quantityChange
                val updatedProduct = it.copy(currentStock = updatedStock)
                productRepository.updateProduct(updatedProduct)
            }
        }
    }
}
