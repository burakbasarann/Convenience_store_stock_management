package com.basaran.casestudy.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.basaran.casestudy.databinding.FragmentDashboardBinding
import com.basaran.casestudy.ui.adapter.LowStockAdapter
import com.basaran.casestudy.ui.adapter.RecentTransactionsAdapter
import com.basaran.casestudy.ui.base.BaseFragment
import com.basaran.casestudy.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var lowStockAdapter: LowStockAdapter
    private lateinit var transactionsAdapter: RecentTransactionsAdapter

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupObservers()
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewLowStock.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTransactions.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        viewModel.lowStockProducts.observe(viewLifecycleOwner) { products ->
            if (products.isEmpty()) {
                binding.emptyLowStockContainer.visibility = View.VISIBLE
                binding.recyclerViewLowStock.visibility = View.GONE
            } else {
                binding.emptyLowStockContainer.visibility = View.GONE
                binding.recyclerViewLowStock.visibility = View.VISIBLE
                lowStockAdapter = LowStockAdapter(products)
                binding.recyclerViewLowStock.adapter = lowStockAdapter
            }
        }

        viewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            if (transactions.isEmpty()) {
                binding.emptyTransactionsContainer.visibility = View.VISIBLE
                binding.recyclerViewTransactions.visibility = View.GONE
            } else {
                binding.emptyTransactionsContainer.visibility = View.GONE
                binding.recyclerViewTransactions.visibility = View.VISIBLE
                transactionsAdapter = RecentTransactionsAdapter(transactions, viewModel.allProducts.value ?: emptyList())
                binding.recyclerViewTransactions.adapter = transactionsAdapter
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoading(true)
                is UiState.Success -> showLoading(false)
                is UiState.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                else -> showLoading(false)
            }
        }
    }
} 