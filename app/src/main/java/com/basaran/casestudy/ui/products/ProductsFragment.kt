package com.basaran.casestudy.ui.products

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.basaran.casestudy.databinding.FragmentProductsBinding
import com.basaran.casestudy.ui.adapter.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsViewModel by viewModels()

    private lateinit var productsAdapter: ProductsAdapter

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
    }

    private fun setupRecyclerView() {
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        productsAdapter = ProductsAdapter { product ->
            navigateToProductDetail(product.id)
        }
        binding.productsRecyclerView.adapter = productsAdapter
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
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
        binding.addProductButton.setOnClickListener {  //TODO Add Product
      //      findNavController().navigate(R.id.action_productsFragment_to_addEditProductFragment)
        }
    }

    private fun navigateToProductDetail(productId: Long) {  //TODO Product Detail
     //   val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment(productId)
     //   findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
