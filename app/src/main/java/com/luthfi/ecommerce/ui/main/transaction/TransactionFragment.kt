package com.luthfi.ecommerce.ui.main.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.gson.Gson
import com.luthfi.ecommerce.MainActivity
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.TransactionResponse
import com.luthfi.ecommerce.databinding.FragmentTransactionBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment : BaseFragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModel()

    private val analytics: FirebaseAnalytics by inject()

    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TransactionAdapter(requireContext()) { data ->
            moveToStatus(data)
        }
        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransaction.adapter = adapter
//        viewModel.transaction()
        viewModel.transactionResponse.observe(viewLifecycleOwner) { response ->
            binding.pbTransaction.isVisible = response is Resource.Loading
            showError(response is Resource.Error)
            if (response != null) {
                when (response) {
                    is Resource.Success -> {
                        adapter.submitList(response.data.data?.reversed())
                        if (response.data.data?.size == 0) {
                            showError(true)
                            binding.tvTransactionErrorCode.text =
                                resources.getString(R.string.http_exception_404_code)
                            binding.tvTransactionErrorMessage.text =
                                resources.getString(R.string.http_exception_404_message)
                        }
                    }

                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        try {
                            val errBody = Gson().fromJson(response.error, ErrorBody::class.java)
                            binding.tvTransactionErrorCode.text =
                                if (errBody.code == 404) getString(R.string.empty) else errBody.code.toString()
                            binding.tvTransactionErrorMessage.text =
                                if (errBody.code == 404) getString(R.string.http_exception_404_message) else errBody.message
                        } catch (e: Exception) {
                            Snackbar.make(binding.root, response.error, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
        binding.btnTransactionRefresh.setOnClickListener {
            viewModel.transaction()
        }
    }

    private fun showError(state: Boolean) {
        binding.ivTransactionError.isVisible = state
        binding.tvTransactionErrorCode.isVisible = state
        binding.tvTransactionErrorMessage.isVisible = state
        binding.btnTransactionRefresh.isVisible = state
    }

    private fun moveToStatus(data: TransactionResponse.Data) {
        (requireActivity() as MainActivity).moveToStatus(data)
        analytics.logEvent("button_click") {
            param("button_name", "btn_review")
        }
        this.viewModelStore.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
