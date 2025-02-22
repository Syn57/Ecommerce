package com.luthfi.ecommerce.ui.main.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.gson.Gson
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductResponse
import com.luthfi.ecommerce.core.data.datasource.api.responses.toProduct
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.databinding.FragmentDetailBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import com.luthfi.ecommerce.utils.format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : BaseFragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModel()

    private val analytics: FirebaseAnalytics by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = DetailFragmentComposeArgs.fromBundle(requireArguments()).productId
        viewModel.saveId(productId)
        binding.btnDetailAllReview.setOnClickListener {
            val navToDetail =
                DetailFragmentComposeDirections.actionDetailFragmentToReviewFragment(productId)
            findNavController().navigate(navToDetail)
        }

        getProduct()

        binding.tbDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getIsFav(productId).observe(viewLifecycleOwner) {
            viewModel.isFav = it
            binding.ivDetailFav.setImageResource(
                when (it) {
                    true -> R.drawable.ic_favorite_red
                    false -> R.drawable.ic_favorite_border
                }
            )
        }

        binding.ivDetailFav.setOnClickListener {
            viewModel.isFav = !viewModel.isFav
            if (viewModel.isFav) {
                viewModel.insertFav(viewModel.product)
                Snackbar.make(
                    binding.root,
                    getString(R.string.success_added_to_wishlist),
                    Snackbar.LENGTH_SHORT
                ).show()
                analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
                    val bs = Bundle()
                    bs.putString(FirebaseAnalytics.Param.ITEM_NAME, viewModel.product.productName)
                    bs.putString(FirebaseAnalytics.Param.ITEM_BRAND, viewModel.product.brand)
                    param(FirebaseAnalytics.Param.ITEMS, arrayOf(bs))
                    param(FirebaseAnalytics.Param.CURRENCY, INDONESIA_CURRENCY)
                    param(
                        FirebaseAnalytics.Param.VALUE,
                        (viewModel.product.productPrice + viewModel.product.variantPrice).toDouble()
                    )
                }
            } else {
                viewModel.deleteFav(viewModel.product.productId)
                Snackbar.make(
                    binding.root,
                    getString(R.string.removed_from_wishlist),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.ivDetailShare.setOnClickListener {
            shareIntent(viewModel.product)
        }

        binding.btnDetailRefresh.setOnClickListener {
            getProduct()
        }

        binding.btnDetailBuy.setOnClickListener {
            val product = viewModel.product
            product.count = 1
            viewModel.checkoutItems.add(product)
            val args =
                DetailFragmentComposeDirections.actionDetailFragmentToCheckoutFragment(
                    viewModel.checkoutItems.toTypedArray()
                )
            analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
                val bs = Bundle()
                bs.putString(FirebaseAnalytics.Param.ITEM_NAME, viewModel.product.productName)
                bs.putString(FirebaseAnalytics.Param.ITEM_BRAND, viewModel.product.brand)
                param(FirebaseAnalytics.Param.ITEMS, arrayOf(bs))
                param(FirebaseAnalytics.Param.CURRENCY, INDONESIA_CURRENCY)
                param(
                    FirebaseAnalytics.Param.VALUE,
                    (viewModel.product.productPrice + viewModel.product.variantPrice).toDouble()
                )
            }
            findNavController().navigate(args)
        }

        if (binding.cgVariants.childCount != 0) {
            if (viewModel.product.variantName == RAM_16_GB) {
                binding.cgVariants.check(binding.cgVariants.getChildAt(viewModel.checkedChipId).id)
            } else {
                binding.cgVariants.check(binding.cgVariants.getChildAt(viewModel.checkedChipId).id)
            }
        }
    }

    private fun shareIntent(product: Product) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "${product.productName}\n${
                    format(
                        product.productPrice
                    )
                }\nLink: www.tokopaerbe.com/${product.productId} "
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun getProduct() {
        viewModel.productResponse.observe(viewLifecycleOwner) { value ->
            binding.pbDetail.isVisible = value is Resource.Loading
            binding.ivDetailError.isVisible = value is Resource.Error
            binding.tvDetailErrorCode.isVisible = value is Resource.Error
            binding.tvDetailErrorMessage.isVisible = value is Resource.Error
            binding.btnDetailRefresh.isVisible = value is Resource.Error
            binding.svDetail.isVisible = value is Resource.Success
            binding.dvdrDetailBot.isVisible = value is Resource.Success
            binding.btnDetailAddChart.isVisible = value is Resource.Success
            binding.btnDetailBuy.isVisible = value is Resource.Success
            if (value != null) {
                when (value) {
                    is Resource.Success -> {
                        showProduct(value.data.data)
                        viewModel.product = value.data.toProduct()
                        binding.cgVariants.setOnCheckedStateChangeListener { _, _ ->
                            viewModel.checkedChipId = binding.cgVariants.checkedChipId
                            viewModel.product.variantName =
                                value.data.data.productVariant[viewModel.checkedChipId].variantName
                            viewModel.product.variantPrice =
                                value.data.data.productVariant[viewModel.checkedChipId].variantPrice
                            binding.tvDetailPrice.text =
                                format(value.data.data.productPrice + viewModel.product.variantPrice)
                        }
                        binding.btnDetailAddChart.setOnClickListener {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val message = viewModel.addChart(viewModel.product, true)
                                if (message == CART_ADDED) {
                                    Snackbar.make(
                                        binding.root,
                                        getString(R.string.success_added_to_chart),
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Snackbar.make(
                                        binding.root,
                                        getString(R.string.out_of_stock),
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
                                val bs = Bundle()
                                bs.putString(
                                    FirebaseAnalytics.Param.ITEM_NAME,
                                    viewModel.product.productName
                                )
                                bs.putString(
                                    FirebaseAnalytics.Param.ITEM_BRAND,
                                    viewModel.product.brand
                                )
                                param(FirebaseAnalytics.Param.ITEMS, arrayOf(bs))
                                param(FirebaseAnalytics.Param.CURRENCY, INDONESIA_CURRENCY)
                                param(
                                    FirebaseAnalytics.Param.VALUE,
                                    (viewModel.product.productPrice + viewModel.product.variantPrice).toDouble()
                                )
                            }
                        }
                        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
                            param(FirebaseAnalytics.Param.CURRENCY, INDONESIA_CURRENCY)
                            param(
                                FirebaseAnalytics.Param.VALUE,
                                value.data.data.productPrice.toDouble()
                            )
                        }
                    }

                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        try {
                            val errBody = Gson().fromJson(value.error, ErrorBody::class.java)
                            binding.tvDetailErrorCode.text =
                                if (errBody.code == 404) getString(R.string.empty) else errBody.code.toString()
                            binding.tvDetailErrorMessage.text =
                                if (errBody.code == 404) getString(R.string.http_exception_404_message) else errBody.message
                        } catch (e: Exception) {
                            Snackbar.make(binding.root, value.error, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showProduct(data: ProductResponse.Data) {
        viewModel.productName = data.productName
        viewModel.productPrice = data.productPrice
        binding.tvDetailProductTitle.text = data.productName
        binding.tvDetailPrice.text = format(data.productPrice + viewModel.product.variantPrice)
        binding.tvDetailSold.text = getString(R.string.sold, data.sale)
        binding.tvDetailRate.text =
            getString(R.string.rate_review, data.productRating, data.totalReview)
        createVariant(data.productVariant)
        binding.tvDetailDescription.text = data.description
        binding.tvDetailScoreRate.text = data.productRating.toString()
        binding.tvDetailRatingPercentage.text =
            getString(R.string.buyers_are_satisfied, data.totalSatisfaction)
        binding.tvDetailRateReview.text =
            getString(R.string.rate_review_num, data.totalRating, data.totalReview)

        // Image Configuration
        val viewPager = binding.vp2DetailImgProduct
        val adapter = ProductImageAdapter(requireContext(), data.image)
        viewPager.adapter = adapter
        val tabLayout = binding.tlDetailImgProduct
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        tabLayout.isVisible = data.image.size != 1
    }

    private fun createVariant(variant: List<ProductResponse.Data.ProductVariant?>?) {
        var i = 0
        variant?.forEach {
            val chip = Chip(requireActivity())
            chip.text = it?.variantName
            chip.id = i
            chip.isCloseIconVisible = false
            val drawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null,
                0,
                com.luthfi.ecommerce.R.style.Widget_App_Chip
            )
            chip.setChipDrawable(drawable)
            chip.isHorizontalFadingEdgeEnabled = false
            binding.cgVariants.addView(chip)
            i++
        }
        if (binding.cgVariants.childCount != 0) {
            if (viewModel.product.variantName == RAM_16_GB) {
                binding.cgVariants.check(binding.cgVariants.getChildAt(viewModel.checkedChipId).id)
            } else {
                binding.cgVariants.check(binding.cgVariants.getChildAt(viewModel.checkedChipId).id)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RAM_16_GB = "RAM 16GB"
        const val INDONESIA_CURRENCY = "IDR"
        const val CART_ADDED = "cartAdded"
    }
}
