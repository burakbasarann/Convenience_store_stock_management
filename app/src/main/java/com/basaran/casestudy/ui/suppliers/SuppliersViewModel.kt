package com.basaran.casestudy.ui.suppliers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.repository.supplier.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuppliersViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
) : ViewModel() {

    val suppliers: LiveData<List<Supplier>> = supplierRepository.getAllSuppliers()
        .asLiveData(viewModelScope.coroutineContext)

    private val _filteredPSuppliers = MutableLiveData<List<Supplier>>()
    val filteredSuppliers: LiveData<List<Supplier>> get() = _filteredPSuppliers

    private val _allSuppliers = MutableStateFlow<List<Supplier>>(emptyList())

    init {
        getSuppliers()
    }

    private fun getSuppliers() {
        viewModelScope.launch {
            supplierRepository.seedInitialData()
            supplierRepository.getAllSuppliers().collect { list ->
                _allSuppliers.value = list
                _filteredPSuppliers.value = list
            }
        }
    }

    fun filterSuppliers(query: String) {
        val suppliers = _allSuppliers.value
        _filteredPSuppliers.value = if (query.isEmpty()) {
            suppliers
        } else {
            suppliers.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }

    fun sortSuppliersAscending() {
        _filteredPSuppliers.value = _filteredPSuppliers.value?.sortedBy { it.name } ?: emptyList()
    }

    fun sortSuppliersDescending() {
        _filteredPSuppliers.value = _filteredPSuppliers.value?.sortedByDescending { it.name } ?: emptyList()
    }
}
