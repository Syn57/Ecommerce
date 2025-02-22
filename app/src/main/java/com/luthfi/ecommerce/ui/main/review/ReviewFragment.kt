package com.luthfi.ecommerce.ui.main.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ReviewResponse
import com.luthfi.ecommerce.databinding.FragmentReviewBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewFragment : BaseFragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewViewModel by viewModel()
    private lateinit var productId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//         Inflate the layout for this fragment
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productId = ReviewFragmentComposeArgs.fromBundle(requireArguments()).productId
        getReview(productId)
        binding.tbReview.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.review(productId)
    }

    private fun getReview(id: String) {
        viewModel.review(id)
        viewModel.reviewResponse2.observe(viewLifecycleOwner) { value ->
            binding.pbReview.isVisible = value is Resource.Loading
            binding.ivReviewError.isVisible = value is Resource.Error
            binding.tvReviewErrorCode.isVisible = value is Resource.Error
            binding.tvReviewErrorMessage.isVisible = value is Resource.Error
            binding.rvReview.isVisible = value is Resource.Success
            if (value != null) {
                when (value) {
                    is Resource.Success -> {
                        showProduct(value.data)
                    }

                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        try {
                            val errBody = Gson().fromJson(value.error, ErrorBody::class.java)
                            binding.tvReviewErrorCode.text =
                                if (errBody.code == 404) getString(R.string.empty) else errBody.code.toString()
                            binding.tvReviewErrorMessage.text =
                                if (errBody.code == 404) getString(R.string.http_exception_404_message) else errBody.message
                        } catch (e: Exception) {
                            Snackbar.make(binding.root, value.error, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    else -> { }
                }
            }
        }
    }

    private fun showProduct(data: ReviewResponse) {
        val adapter = ReviewAdapter(requireContext(), data.data)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.layoutManager = layoutManager
        binding.rvReview.adapter = adapter
    }
}
