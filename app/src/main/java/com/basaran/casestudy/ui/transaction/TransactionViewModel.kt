package com.basaran.casestudy.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.repository.transaction.TransactionRepository
import com.basaran.casestudy.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun getAllTransactions() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            transactionRepository.getAllTransactions().collect { transactionList ->
                _transactions.postValue(transactionList)
                _state.value = UiState.Success
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
}
