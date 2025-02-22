package com.luthfi.ecommerce.ui.main.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.databinding.FragmentChartBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import com.luthfi.ecommerce.utils.format
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChartFragment : BaseFragment() {

    private val viewModel: ChartViewModel by viewModel()
    private val analytics: FirebaseAnalytics by inject()
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
        initObserver()
    }

    private fun initView() {
        // Setup recycler view stuff
        adapter = ChartAdapter(requireContext(), { id ->
            deleteItem(id)
        }, { chart ->
            increaseChart(chart)
        }, { chart ->
            decreaseChart(chart)
        }, { chart ->
            checkItem(chart)
        })
        binding.rvChart.itemAnimator = null
        binding.rvChart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChart.adapter = adapter
    }

    private fun initObserver() {
        // Data observer
        viewModel.getAllChart.observe(viewLifecycleOwner) { listChart ->
            showError(listChart!!.isEmpty())
            showChart(listChart.isEmpty(), listChart)
            val isCheckTotal = listChart.filter { it.isChecked }.size
            if (isCheckTotal > 0) {
                binding.btnChartEraseAll.visibility = View.VISIBLE
                binding.btnChartBuy.isEnabled = true
            } else {
                binding.btnChartEraseAll.visibility = View.GONE
                binding.btnChartBuy.isEnabled = false
            }
            binding.cbChartCheckAll.isChecked = isCheckTotal == listChart.size
        }
    }

    private fun initAction() {
        binding.btnChartEraseAll.setOnClickListener {
            viewModel.deleteAllChecked()
        }
        binding.cbChartCheckAll.setOnClickListener {
            viewModel.allCheck(binding.cbChartCheckAll.isChecked)
        }
        binding.tbChart.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnChartBuy.setOnClickListener {
            val args =
                ChartFragmentDirections.actionChartFragmentToCheckoutFragment(viewModel.checkoutItems.toTypedArray())
            logBeginCheckout()
            findNavController().navigate(args)
        }
    }

    private fun showChart(state: Boolean, listChart: List<Product>) {
        if (!state) {
            adapter.submitList(listChart)
            var totalPay = 0
            var logTotalPay = 0
            val logChart = mutableListOf<Bundle>()
            viewModel.checkoutItems.clear()
            listChart.forEach {
                if (it.isChecked) {
                    totalPay += ((it.variantPrice + it.productPrice) * it.count)
                    viewModel.checkoutItems.add(it)
                }
                val bs = Bundle()
                bs.putString(FirebaseAnalytics.Param.ITEM_NAME, it.productName)
                bs.putString(FirebaseAnalytics.Param.ITEM_BRAND, it.brand)
                bs.putLong(FirebaseAnalytics.Param.QUANTITY, it.count.toLong())
                logChart.add(bs)
                logTotalPay += ((it.variantPrice + it.productPrice) * it.count)
            }
            logViewCart(logChart, logTotalPay)
            binding.tvChartTotalPay.text = format(totalPay)
        }
    }

    // Functions for adapter
    private fun decreaseChart(product: Product) {
        viewModel.updateChart(product, false)
    }

    private fun checkItem(product: Product) {
        product.isChecked = !product.isChecked
        viewModel.updateIsChecked(product.productId, product.isChecked)
    }

    private fun deleteItem(product: Product) {
        viewModel.deleteChart(product.productId)
        logRemoveChart(product)
    }

    private fun increaseChart(product: Product) {
        viewModel.updateChart(product, true)
    }

    // Firebase analytics
    private fun logRemoveChart(product: Product) {
        analytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART) {
            param(FirebaseAnalytics.Param.CURRENCY, getString(R.string.idr_currency))
            val bs = Bundle()
            bs.putString(FirebaseAnalytics.Param.ITEM_NAME, product.productName)
            bs.putString(FirebaseAnalytics.Param.ITEM_BRAND, product.brand)
            bs.putLong(FirebaseAnalytics.Param.QUANTITY, product.count.toLong())
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(bs))
            param(
                FirebaseAnalytics.Param.VALUE,
                (product.productPrice + product.variantPrice).toDouble()
            )
        }
    }

    private fun logViewCart(logChart: MutableList<Bundle>, logTotalPay: Int) {
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_CART) {
            param(FirebaseAnalytics.Param.ITEMS, logChart.toTypedArray())
            param(FirebaseAnalytics.Param.CURRENCY, getString(R.string.idr_currency))
            param(FirebaseAnalytics.Param.VALUE, logTotalPay.toDouble())
        }
    }

    private fun logBeginCheckout() {
        analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
            val logCheckout = mutableListOf<Bundle>()
            var logTotalCheckout = 0
            viewModel.checkoutItems.forEach {
                val bs = Bundle()
                bs.putString(FirebaseAnalytics.Param.ITEM_NAME, it.productName)
                bs.putString(FirebaseAnalytics.Param.ITEM_BRAND, it.brand)
                bs.putLong(FirebaseAnalytics.Param.QUANTITY, it.count.toLong())
                logCheckout.add(bs)
                logTotalCheckout += ((it.variantPrice + it.productPrice) * it.count)
            }
            param(FirebaseAnalytics.Param.ITEMS, logCheckout.toTypedArray())
            param(FirebaseAnalytics.Param.CURRENCY, getString(R.string.idr_currency))
            param(FirebaseAnalytics.Param.VALUE, logTotalCheckout.toDouble())
        }
    }

    // UI error handling
    private fun showError(state: Boolean) {
        // Visible view
        binding.ivChartError.isVisible = state
        binding.tvChartErrorCode.isVisible = state
        binding.tvChartErrorMessage.isVisible = state
        // Invisible view
        binding.rvChart.isVisible = !state
        binding.dvdrChart1.isVisible = !state
        binding.cbChartCheckAll.isVisible = !state
        binding.tvChooseAll.isVisible = !state
        binding.btnChartEraseAll.isVisible = !state
        binding.dvdrChart2.isVisible = !state
        binding.tvChartTotalPay.isVisible = !state
        binding.tvChartTotalTitle.isVisible = !state
        binding.btnChartBuy.isVisible = !state
    }
}
