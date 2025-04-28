package com.basaran.casestudy.ui.products.addeditproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.databinding.FragmentAddOrEditProductBinding
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddOrEditProductFragment : Fragment() {

    private var _binding: FragmentAddOrEditProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddOrEditProductViewModel by viewModels()
    private lateinit var editProduct: Product

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Product>("product")?.let {
            editProduct = it
            fillProductDetails(editProduct)
        }?: run {
            editProduct = Product()
        }
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.apply {
            saveButton.setOnClickListener {
                saveProduct()
            }
            cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        showLoading(false)
                        handleSuccess()
                    }
                    is UiState.Loading -> {
                        showLoading(true)
                    }
                    is UiState.Error ->  {
                        showLoading(false)
                        showError(state.message)
                    }
                    is UiState.Idle -> {}
                }
            }

        }
        lifecycleScope.launch {
            viewModel.validationErrors.collect { errors ->
                showValidationErrors(errors)
            }
        }
    }

    private fun saveProduct() {
        binding.apply {
            val name = productNameEditText.text.toString()
            val description = productDescriptionEditText.text.toString()
            val price = productPriceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val category = productCategoryEditText.text.toString()
            val barcode = productBarcodeEditText.text.toString()
            val currentStock = productStockEditText.text.toString().toIntOrNull() ?: 0
            val minStock = productMinStockEditText.text.toString().toIntOrNull() ?: 0

            val product = Product(
                id = editProduct.id,
                userId = UserManager.getUserId(),
                name = name,
                description = description,
                price = price,
                category = category,
                barcode = barcode,
                supplierId = 0L,
                currentStock = currentStock,
                minStock = minStock
            )
            viewModel.saveProduct(product)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.saveButton.isEnabled = !isLoading
    }

    private fun handleSuccess() {
        findNavController().navigateUp()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showValidationErrors(errors: Set<AddOrEditProductViewModel.ValidationError>) {
        binding.apply {
            productNameInput.error = errors.find { it.field == AddOrEditProductViewModel.Field.NAME }?.message
            productPriceInput.error = errors.find { it.field == AddOrEditProductViewModel.Field.PRICE }?.message
            productCategoryInput.error = errors.find { it.field == AddOrEditProductViewModel.Field.CATEGORY }?.message
            productBarcodeInput.error = errors.find { it.field == AddOrEditProductViewModel.Field.BARCODE }?.message
            productMinStockInput.error = errors.find { it.field == AddOrEditProductViewModel.Field.MIN_STOCK }?.message
        }
    }

    private fun fillProductDetails(product: Product) {
        binding.productNameEditText.setText(product.name)
        binding.productDescriptionEditText.setText(product.description)
        binding.productPriceEditText.setText(product.price.toString())
        binding.productCategoryEditText.setText(product.category)
        binding.productBarcodeEditText.setText(product.barcode)
        binding.productStockEditText.setText(product.currentStock.toString())
        binding.productMinStockEditText.setText(product.minStock.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}