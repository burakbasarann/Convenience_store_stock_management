package com.basaran.casestudy.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.basaran.casestudy.R
import com.basaran.casestudy.databinding.FragmentLoginBinding
import com.basaran.casestudy.ui.base.BaseFragment
import com.basaran.casestudy.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.loginButton.isEnabled = !isLoading
        binding.registerButton.isEnabled = !isLoading
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(username, password)
        }

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.register(username, password)
        }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    setLoadingState(true)
                }
                is UiState.Success -> {
                    setLoadingState(false)
                    findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                }
                is UiState.Error -> {
                    setLoadingState(false)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Idle -> {}
            }
        }
    }
} 