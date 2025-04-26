package com.basaran.casestudy.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.databinding.FragmentDashboardBinding
import com.basaran.casestudy.ui.adapter.LowStockAdapter
import com.basaran.casestudy.ui.adapter.RecentTransactionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var lowStockAdapter: LowStockAdapter
    private lateinit var recentTransactionsAdapter: RecentTransactionsAdapter
    private var allProducts: List<Product> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()
        setupBottomNavigation()
    }

    private fun setupRecyclerViews() {
        binding.lowStockRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recentTransactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModel() {
        viewModel.lowStockProducts.observe(viewLifecycleOwner) { products ->
            allProducts = products
            lowStockAdapter = LowStockAdapter(products)
            binding.lowStockRecyclerView.adapter = lowStockAdapter
        }

        viewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            recentTransactionsAdapter = RecentTransactionsAdapter(transactions, allProducts)
            binding.recentTransactionsRecyclerView.adapter = recentTransactionsAdapter
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
             true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 