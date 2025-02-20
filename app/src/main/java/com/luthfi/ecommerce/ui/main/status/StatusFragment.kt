package com.luthfi.ecommerce.ui.main.status

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.gson.Gson
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.databinding.FragmentStatusBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import com.luthfi.ecommerce.utils.format
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatusFragment : BaseFragment() {

    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatusViewModel by viewModel()
    private val analytics: FirebaseAnalytics by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val invoiceId = StatusFragmentArgs.fromBundle(requireArguments()).invoiceId
        val status = StatusFragmentArgs.fromBundle(requireArguments()).status
        val date = StatusFragmentArgs.fromBundle(requireArguments()).date
        val time = StatusFragmentArgs.fromBundle(requireArguments()).time
        val payment = StatusFragmentArgs.fromBundle(requireArguments()).payment
        val total = StatusFragmentArgs.fromBundle(requireArguments()).total
        val rating = StatusFragmentArgs.fromBundle(requireArguments()).rating
        val review =
            if (StatusFragmentArgs.fromBundle(requireArguments()).review == null) {
                ""
            } else {
                StatusFragmentArgs.fromBundle(
                    requireArguments()
                ).review
            }

//        if (rating != -1) {
        binding.rbStatus.rating = rating.toFloat()
//            binding.rbStatus.setIsIndicator(true)
//        }

        if (review != null) {
            binding.edtStatusReview.setText(review)
//            binding.edtStatusReview.isEnabled = false
        }

        binding.tvStatusTransactionIdValue.text = invoiceId
        binding.tvStatusTransactionStatusValue.text =
            if (status) getString(R.string.succeed) else getString(
                R.string.failed
            )
        binding.tvStatusTransactionDateValue.text = date
        binding.tvStatusTransactionTimeValue.text = time
        binding.tvStatusTransactionPaymentMethodValue.text = payment
        binding.tvStatusTransactionTotalPaymentValue.text = format(total)
        binding.rbStatus.stepSize = 1f

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (StatusFragmentArgs.fromBundle(requireArguments()).flagDestination == FLAG_CHECKOUT) handleBackNavigation() else findNavController().navigateUp()
                }
            }
        )

        binding.btnStatusDone.setOnClickListener {
            analytics.logEvent("button_click") {
                param("button_name", "btn_status_done")
            }
            if (StatusFragmentArgs.fromBundle(requireArguments()).flagDestination != FLAG_CHECKOUT && binding.rbStatus.rating == rating.toFloat() && binding.edtStatusReview.text.toString() == review.toString()) {
                findNavController().navigateUp()
            }
            if (binding.edtStatusReview.text.toString() != "" || binding.rbStatus.numStars != 0) {
                viewModel.ratingBody.rating = binding.rbStatus.rating.toInt()
                if (binding.rbStatus.rating.toInt() != 0) {
                    viewModel.ratingBody.rating = binding.rbStatus.rating.toInt()
                } else {
                    viewModel.ratingBody.rating = null
                }
                if (binding.edtStatusReview.text.toString() != "") {
                    viewModel.ratingBody.review = binding.edtStatusReview.text.toString()
                } else {
                    viewModel.ratingBody.review = null
                }
                viewModel.ratingBody.invoiceId = invoiceId
                viewModel.rating()
                viewModel.ratingResponse.observe(viewLifecycleOwner) { response ->
                    binding.btnStatusDone.visibility =
                        if (response !is Resource.Loading) View.VISIBLE else View.INVISIBLE
                    binding.pbStatus.visibility =
                        if (response is Resource.Loading) View.VISIBLE else View.INVISIBLE
                    if (response != null) {
                        when (response) {
                            is Resource.Success -> {
                                handleBackNavigation()
                            }

                            is Resource.Loading -> {}
                            is Resource.Error -> {
                                try {
                                    val errBody =
                                        Gson().fromJson(response.error, ErrorBody::class.java)
                                    Snackbar.make(
                                        binding.root,
                                        errBody.message,
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()
                                } catch (e: Exception) {
                                    Snackbar.make(
                                        binding.root,
                                        response.error,
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }
                    }
                }
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun handleBackNavigation() {
        if (StatusFragmentArgs.fromBundle(requireArguments()).flagDestination == FLAG_CHECKOUT) {
            val args = StatusFragmentDirections.actionStatusFragmentToMainFragment()
            args.flag = FLAG_HOME
            findNavController().navigate(args)
        } else {
            val args = StatusFragmentDirections.actionStatusFragmentToMainFragment()
            findNavController().navigate(R.id.action_statusFragment_to_mainFragment)
        }
    }

    companion object {
        const val FLAG_HOME = "home"
        const val FLAG_CHECKOUT = "checkout"
    }
}
