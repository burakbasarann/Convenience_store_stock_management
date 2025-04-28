package com.basaran.casestudy.ui.suppliers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.repository.supplier.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuppliersViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
) : ViewModel() {

    private val _suppliers = MutableLiveData<List<Supplier>>()
    val suppliers: LiveData<List<Supplier>> get() = _suppliers

    init {
        getSuppliers()
    }

    private fun getSuppliers() {
        viewModelScope.launch {
            supplierRepository.seedInitialData()
            supplierRepository.getAllSuppliers().collect { list ->
                _suppliers.postValue(list)
            }
        }
    }
}
