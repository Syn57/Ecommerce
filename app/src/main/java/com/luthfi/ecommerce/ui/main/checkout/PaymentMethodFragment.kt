package com.luthfi.ecommerce.ui.main.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.gson.Gson
import com.luthfi.ecommerce.core.data.datasource.api.responses.PaymentResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.toPayment
import com.luthfi.ecommerce.core.domain.model.Payment
import com.luthfi.ecommerce.databinding.FragmentPaymentMethodBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class PaymentMethodFragment : BaseFragment() {

    private var _binding: FragmentPaymentMethodBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PaymentAdapter

    private val remoteConfig: FirebaseRemoteConfig by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        binding.tbPayment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
//        //Api payment
//        viewModel.getPaymentOptions()
//        viewModel.paymentResponse.observe(viewLifecycleOwner){ response ->
//            binding.pbPayment.isVisible = response is Resource.Loading
//            binding.rvPaymentTva.isVisible = response is Resource.Success
//            if( response != null){
//                when (response) {
//                    is Resource.Success -> {
//                        adapter.submitList(response.data.data)
//                    }
//                    is Resource.Loading -> {}
//                    is Resource.Error -> {
//                        try {
//                            val error = response.error.split("~")
// //                            binding.tvPaymeErrorCode.text =
// //                                if (error[0] == "404") getString(R.string.empty) else error[0]
// //                            binding.tvDetailErrorMessage.text =
// //                                if (error[0] == "404") getString(R.string.http_exception_404_message) else error[1]
//                            Snackbar.make(binding.root, error[1], Snackbar.LENGTH_SHORT).show()
//                        } catch (e: Exception) {
//                            Snackbar.make(binding.root, e.toString(), Snackbar.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun initView() {
        adapter = PaymentAdapter(requireContext()) { data ->
            sendData(data)
        }
        binding.rvPaymentTva.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPaymentTva.adapter = adapter
    }

    private fun initData() {
        // Firebase remote config
        val paymentMethod = remoteConfig.getString(FIREBASE_KEY_PAYMENT)
        showPayments(paymentMethod)
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(FIREBASE_KEY_PAYMENT)) {
                    remoteConfig.activate().addOnCompleteListener {
                        val paymentMethodNew = remoteConfig.getString(FIREBASE_KEY_PAYMENT)
                        showPayments(paymentMethodNew)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
            }
        })
    }

    private fun showPayments(payments: String) {
        val myPayments = Gson().fromJson(payments, PaymentResponse::class.java)
        adapter.submitList(myPayments.data.toPayment())
    }

    private fun sendData(data: Payment.Item) {
        setFragmentResult(
            PAYMENT_METHOD,
            bundleOf(SELECTED_PAYMENT to data.label, SELECTED_PAYMENT_LOGO to data.image)
        )
        findNavController().popBackStack()
    }

    companion object {
        private const val PAYMENT_METHOD = "payment_method"
        private const val SELECTED_PAYMENT = "selected_payment"
        private const val SELECTED_PAYMENT_LOGO = "selected_payment_logo"
        private const val FIREBASE_KEY_PAYMENT = "payment"
    }
}
