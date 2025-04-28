package com.basaran.casestudy.ui.products

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.basaran.casestudy.R
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.databinding.FragmentProductsBinding
import com.basaran.casestudy.ui.adapter.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsViewModel by viewModels()

    private lateinit var productsAdapter: ProductsAdapter
    private var isInEditMode = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupSearch()
        setupAddProductButton()
        setupEditProductButton()
    }

    private fun setupRecyclerView() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.filter_options_product, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.filterSpinner.adapter = adapter;

        binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (selectedItemView == null) return
                when (position) {
                    0 -> viewModel.sortProductsAscending()
                    1 -> viewModel.sortProductsDescending()
                    2 -> viewModel.increasingPriceProduct()
                    3 -> viewModel.descreasingPriceProduct()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        productsAdapter = ProductsAdapter(
            onItemClick = { product ->
                navigateToProductDetail(product.id)
            },
            onEditClick = { product ->
                navigateToEditProduct(product)
            }
        )
        binding.productsRecyclerView.adapter = productsAdapter
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productsAdapter.submitList(products)
        }
        viewModel.filteredProducts.observe(viewLifecycleOwner) { products ->
            productsAdapter.submitList(products)
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { //TODO Buraya Bak
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { //TODO Buraya Bak
            }

            override fun afterTextChanged(editable: Editable?) {
                viewModel.filterProducts(editable.toString())
            }
        })
    }


    private fun setupAddProductButton() {
        binding.addProductFab.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_addOrEditProductFragment)
        }
    }

    private fun setupEditProductButton() {
        binding.editProductFab.setOnClickListener {
            isInEditMode = !isInEditMode
            productsAdapter.setEditMode(isInEditMode)
        }
    }

    override fun onResume() {
        super.onResume()
        isInEditMode = false
    }

    private fun navigateToProductDetail(productId: Long) {  //TODO Product Detail
     //   val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment(productId)
     //   findNavController().navigate(action)
    }

    private fun navigateToEditProduct(product: Product) {
        val bundle = Bundle()
        bundle.putParcelable("product", product)
        findNavController().navigate(R.id.action_productsFragment_to_addOrEditProductFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
