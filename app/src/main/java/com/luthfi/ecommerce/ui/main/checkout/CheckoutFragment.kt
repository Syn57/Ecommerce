package com.luthfi.ecommerce.ui.main.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.gson.Gson
import com.luthfi.ecommerce.R
import com.ecommerce.uiassets.R as RUI
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.FulfillmentBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.FulfillmentResponse
import com.luthfi.ecommerce.databinding.FragmentCheckoutBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import com.luthfi.ecommerce.utils.format
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutFragment : BaseFragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModel()

    private lateinit var adapter: CheckoutAdapter

    private val analytics: FirebaseAnalytics by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = CheckoutFragmentArgs.fromBundle(requireArguments()).checkoutItems
        viewModel.checkoutProduct.postValue(items.toList())
        initView()
        initObserver()
        initAction()
        initListener()
    }

    private fun initView() {
        // Setup recycler view
        adapter = CheckoutAdapter(requireContext(), { position ->
            increaseChart(position)
        }, { position ->
            decreaseChart(position)
        })
        binding.rvCheckout.itemAnimator = null
        binding.rvCheckout.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCheckout.adapter = adapter
    }

    private fun initObserver() {
        viewModel.checkoutProduct.observe(viewLifecycleOwner) { chart ->
            var totalPay = 0
            val listItem = mutableListOf<FulfillmentBody.Item>()
            chart.forEach {
                totalPay += ((it.productPrice + it.variantPrice) * it.count)
                listItem.add(
                    FulfillmentBody.Item(
                        productId = it.productId,
                        variantName = it.variantName,
                        quantity = it.count
                    )
                )
            }
            viewModel.addItemToBuy(listItem)
            binding.tvCheckoutTotalPay.text = format(totalPay)
            adapter.submitList(chart)
        }

        viewModel.paymentMethod.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.tvCheckoutChoosePayment.text = it[SELECTED_PAYMENT]
                Glide.with(requireContext())
                    .load(it[SELECTED_PAYMENT_LOGO])
                    .error(RUI.drawable.ic_add_card)
                    .placeholder(RUI.drawable.ic_add_card)
                    .into(binding.ivCheckoutAddCard)
            }
        }

        viewModel.fBody.observe(viewLifecycleOwner) {
            binding.btnCheckoutBuy.isEnabled = it.payment != null
        }
    }

    private fun initAction() {
        binding.tbCheckout.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.cvCheckoutPaymentMethod.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentMethodFragment)
        }

        binding.btnCheckoutBuy.setOnClickListener {
            viewModel.fulfillment()
            viewModel.fulfillmentResponse.observe(viewLifecycleOwner) { fulfilment ->
                binding.pbCheckout.visibility =
                    if (fulfilment is Resource.Loading) View.VISIBLE else View.INVISIBLE
                binding.btnCheckoutBuy.visibility =
                    if (fulfilment !is Resource.Loading) View.VISIBLE else View.INVISIBLE
                if (fulfilment != null) {
                    when (fulfilment) {
                        is Resource.Success -> {
                            moveToStatus(fulfilment.data)
                            logPurchase(fulfilment.data)
                            viewModel.deleteAllChecked()
                        }

                        is Resource.Loading -> {}
                        is Resource.Error -> {
                            try {
                                val errBody =
                                    Gson().fromJson(fulfilment.error, ErrorBody::class.java)
                                Snackbar.make(binding.root, errBody.message, Snackbar.LENGTH_SHORT)
                                    .show()
                            } catch (e: Exception) {
                                Snackbar.make(binding.root, fulfilment.error, Snackbar.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun initListener() {
        // Payment method listener
        setFragmentResultListener(PAYMENT_METHOD) { _, bundle ->
            viewModel.addPaymentMethod(bundle.getString(SELECTED_PAYMENT))
            viewModel.paymentMethod.postValue(
                hashMapOf(
                    SELECTED_PAYMENT to bundle.getString(SELECTED_PAYMENT),
                    SELECTED_PAYMENT_LOGO to bundle.getString(SELECTED_PAYMENT_LOGO)
                )
            )
            logAddPaymentInfo(bundle.getString(SELECTED_PAYMENT))
        }
    }

    private fun moveToStatus(fulfilment: FulfillmentResponse) {
        val args = CheckoutFragmentDirections.actionCheckoutFragmentToStatusFragment(
            fulfilment.data!!.invoiceId!!,
            fulfilment.data!!.status!!,
            fulfilment.data!!.date!!,
            fulfilment.data!!.time!!,
            fulfilment.data!!.payment!!,
            fulfilment.data!!.total!!,
            "",
            0,
            FLAG_DESTINATION_CHECKOUT
        )
        findNavController().navigate(args)
    }

    // Functions for adapter
    private fun increaseChart(position: Int) {
        viewModel.increaseChart(position)
    }

    private fun decreaseChart(position: Int) {
        viewModel.decreaseChart(position)
    }

    // Firebase analytics
    private fun logAddPaymentInfo(payment: String?) {
        analytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO) {
            param(FirebaseAnalytics.Param.PAYMENT_TYPE, "$payment")
        }
    }

    private fun logPurchase(fulfilment: FulfillmentResponse) {
        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE) {
            param(FirebaseAnalytics.Param.CURRENCY, getString(RUI.string.idr_currency))
            param(FirebaseAnalytics.Param.VALUE, fulfilment.data?.total?.toDouble() ?: 0.0)
            param(FirebaseAnalytics.Param.PAYMENT_TYPE, "${fulfilment.data?.payment}")
            param(FirebaseAnalytics.Param.TRANSACTION_ID, "${fulfilment.data?.invoiceId}")
            param(FirebaseAnalytics.Param.PAYMENT_TYPE, "${fulfilment.data?.payment}")
            val logProducts = mutableListOf<Bundle>()
            viewModel.checkoutProduct.value?.forEach {
                val bs = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, it.productId)
                    putString(FirebaseAnalytics.Param.ITEM_NAME, it.productName)
                    putString(FirebaseAnalytics.Param.ITEM_CATEGORY, it.brand)
                    putString(
                        FirebaseAnalytics.Param.ITEM_VARIANT,
                        it.variantName
                    )
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, it.brand)
                    putLong(FirebaseAnalytics.Param.QUANTITY, it.count.toLong())
                    putDouble(
                        FirebaseAnalytics.Param.PRICE,
                        ((it.productPrice + it.variantPrice).toDouble()) / 1_000_000
                    )
                }
                logProducts.add(bs)
            }
            param(FirebaseAnalytics.Param.ITEMS, logProducts.toTypedArray())
        }
    }

    companion object {
        private const val PAYMENT_METHOD = "payment_method"
        private const val SELECTED_PAYMENT = "selected_payment"
        private const val SELECTED_PAYMENT_LOGO = "selected_payment_logo"
        private const val FLAG_DESTINATION_CHECKOUT = "checkout"
    }
}
