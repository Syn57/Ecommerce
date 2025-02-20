package com.luthfi.ecommerce.ui.main.store

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.chip.Chip
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.luthfi.ecommerce.MainActivity
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.core.data.datasource.api.body.ProductsBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductsResponse
import com.luthfi.ecommerce.databinding.FragmentStoreBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import com.luthfi.ecommerce.utils.formatBottomSheet
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.io.IOException


class StoreFragment : BaseFragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by viewModel()

    private lateinit var adapter: ProductPagingAdapter
    private lateinit var footerAdapter: LoadStateAdapterProduct
    private lateinit var lmLinear: LinearLayoutManager
    private lateinit var lmGrid: GridLayoutManager
    private val analytics: FirebaseAnalytics by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        // First api hit
        getProduct()

        // Refresh data from API
        binding.rlStore.setOnRefreshListener {
            adapter.refresh()
            binding.rlStore.isRefreshing = false
        }

        // Reset button
        binding.btnStoreRefresh.setOnClickListener {
            if (binding.btnStoreRefresh.text.toString() == getString(R.string.reset)) {
                viewModel.productsBody.postValue(
                    ProductsBody(
                        null,
                        null,
                        null,
                        null,
                        null,
                        PAGING_PAGE_LIMIT,
                        PAGING_PAGE
                    )
                )
                binding.edtSearch.text = null
            } else {
                adapter.refresh()
            }
        }
    }

    private fun initView() {
        // Set up recyclerview and paging adapter
        adapter = ProductPagingAdapter(requireContext())
        lmLinear = LinearLayoutManager(requireContext())
        lmGrid = GridLayoutManager(requireContext(), 2)
        footerAdapter = LoadStateAdapterProduct()
        lmGrid.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
        adapter.addLoadStateListener { loadState ->
            showLoading(loadState.refresh is LoadState.Loading)
            val errorState = when {
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            when (val throwable = errorState?.error) {
                is IOException -> {
                    showError(
                        getString(R.string.io_exception_code),
                        getString(R.string.io_exception_message)
                    )
                }

                is HttpException -> {
                    if (throwable.code() == 404) {
                        showError(
                            getString(R.string.http_exception_404_code),
                            getString(R.string.http_exception_404_message)
                        )
                    } else {
                        showError(throwable.code().toString(), throwable.message().toString())
                    }
                }
            }
        }

        // Switching layout type
        binding.ivStoreLayoutType.setOnClickListener {
            viewModel.isGridLayout = !viewModel.isGridLayout
            binding.ivStoreLayoutType.setImageResource(
                when (viewModel.isGridLayout) {
                    true -> R.drawable.ic_list_grid
                    else -> R.drawable.ic_list_linear
                }
            )
            binding.rvStoreProducts.layoutManager = if (viewModel.isGridLayout) lmGrid else lmLinear
            adapter.setLayoutType(viewModel.isGridLayout)
            binding.rvStoreProducts.adapter = adapter.withLoadStateFooter(
                footer = footerAdapter
            )
            adapter.notifyItemChanged(0)
//            adapter.notifyDataSetChanged()
        }

        // Filter feature
        binding.chipStoreFilter.setOnClickListener {
            val botSheet = SearchBottomSheetFragment()
            val bsBundle = bundleOf(
                BUNDLE_KEY_SORT to viewModel.productsBody.value?.sort,
                BUNDLE_KEY_CATEGORY to viewModel.productsBody.value?.brand,
                BUNDLE_KEY_LOWEST to viewModel.productsBody.value?.lowest.toString(),
                BUNDLE_KEY_HIGHEST to viewModel.productsBody.value?.highest.toString()
            )
            botSheet.arguments = bsBundle
            botSheet.show(childFragmentManager, TAG_BOTTOM_SHEET)
            setChipsBottomSheet()
        }

        // Set chips and search appearance
        viewModel.productsBody.observe(viewLifecycleOwner) {
            createChips(
                listOf(
                    it.sort,
                    it.brand,
                    if (it.lowest != null) {
//                        getString(
//                            R.string.rp_lowest,
//                            viewModel.productsBody.value?.lowest.toString()
//                        )
                        "< Rp${formatBottomSheet(viewModel.productsBody.value?.lowest.toString())}"
                    } else {
                        null
                    },
                    if (it.highest != null) {
//                        getString(
//                            R.string.rp_highest,
//                            viewModel.productsBody.value?.highest.toString()
//                        )
                        "> Rp${formatBottomSheet(viewModel.productsBody.value?.highest.toString())}"
                    } else {
                        null
                    }
                )
            )
            binding.edtSearch.setText(it.search)
        }

        // Search feature
        binding.edtSearch.setOnClickListener {
            val dialogBundle = Bundle()
            dialogBundle.putString(BUNDLE_KEY_SEARCH, viewModel.productsBody.value?.search)
            val searchDialog = SearchDialogFragment()
            searchDialog.arguments = dialogBundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction
                .add(android.R.id.content, searchDialog)
                .addToBackStack(null)
                .commit()
            getSearchKeyword()
        }
    }

    private fun setChipsBottomSheet() {
        childFragmentManager.setFragmentResultListener(
            REQUEST_KEY_BOTTOM_SHEET,
            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.updateFilter(
                bundle.getString(BUNDLE_KEY_SORT),
                bundle.getString(BUNDLE_KEY_CATEGORY),
                bundle.getString(BUNDLE_KEY_LOWEST)?.toInt(),
                bundle.getString(BUNDLE_KEY_HIGHEST)?.toInt()
            )
        }
    }

    private fun createChips(chips: List<String?>) {
        binding.chipStoreGroup.removeAllViews()
        // Add chip
        chips.forEach {
            if (it != null) {
                val chip = Chip(requireActivity())
                chip.text = it
                chip.isCloseIconVisible = false
                binding.chipStoreGroup.addView(chip)
            }
        }
    }

    private fun getSearchKeyword() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            REQUEST_KEY_SEARCH_DIALOG,
            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.updateSearch(bundle.getString(BUNDLE_KEY_SEARCH))
        }
    }

    private fun getProduct() {
        viewModel.products.observe(viewLifecycleOwner) {
            showProducts(it)
        }
    }

    private fun showProducts(products: PagingData<ProductsResponse.Data.Item>) {
        binding.rvStoreProducts.layoutManager = if (viewModel.isGridLayout) lmGrid else lmLinear
        adapter.setLayoutType(viewModel.isGridLayout)
        adapter.setOnItemClickCallback(object : ProductPagingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ProductsResponse.Data.Item) {
                (requireActivity() as MainActivity).moveToDetail(data.productId, "RAM 16GB")
                analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                    param(FirebaseAnalytics.Param.ITEM_LIST_NAME, data.productName)
                }
            }
        })
        binding.rvStoreProducts.adapter = adapter.withLoadStateFooter(
            footer = footerAdapter
        )
        adapter.submitData(lifecycle, products)
        adapter.addOnPagesUpdatedListener {
            analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST) {
                val items = mutableListOf<Bundle>()
                if (adapter.snapshot().items.isNotEmpty()) {
                    adapter.snapshot().items.forEach {
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, it.productName)
                        bundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, it.brand)
                        bundle.putDouble(
                            FirebaseAnalytics.Param.PRICE,
                            it.productPrice.toDouble() / 1_000_000
                        )
                        items.add(bundle)
                    }
                    param(FirebaseAnalytics.Param.ITEMS, items.toTypedArray())
                    param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "products")
                }
            }
        }
    }

    private fun showLoading(loadState: Boolean) {
        binding.apply {
            // Visible component
            if (!viewModel.isGridLayout) {
                shimmerLinear.skelShimmerLayoutLinear.visibility =
                    if (loadState) View.VISIBLE else View.INVISIBLE
            } else {
                shimmerGrid.skelShimmerLayoutGrid.visibility =
                    if (loadState) View.VISIBLE else View.INVISIBLE
            }
            // Invisible component
            rvStoreProducts.visibility = if (loadState) View.INVISIBLE else View.VISIBLE
            chipStoreFilter.visibility = if (loadState) View.INVISIBLE else View.VISIBLE
            ivStoreLayoutType.visibility = if (loadState) View.INVISIBLE else View.VISIBLE
            dvdr1.visibility = if (loadState) View.INVISIBLE else View.VISIBLE
            chipStoreGroup.visibility = if (loadState) View.INVISIBLE else View.VISIBLE
            ivStoreError.visibility = View.INVISIBLE
            tvStoreErrorCode.visibility = View.INVISIBLE
            tvStoreErrorMessage.visibility = View.INVISIBLE
            btnStoreRefresh.visibility = View.INVISIBLE
        }
    }

    private fun showError(code: String, message: String) {
        binding.apply {
            // Invisible component
            rvStoreProducts.visibility = View.INVISIBLE
            chipStoreFilter.visibility = View.INVISIBLE
            ivStoreLayoutType.visibility = View.INVISIBLE
            dvdr1.visibility = View.INVISIBLE
            chipStoreGroup.visibility = View.INVISIBLE
            // Visible component
            ivStoreError.visibility = View.VISIBLE
            tvStoreErrorCode.visibility = View.VISIBLE
            tvStoreErrorMessage.visibility = View.VISIBLE
            btnStoreRefresh.visibility = View.VISIBLE
            // Data
            btnStoreRefresh.text =
                if (code == getString(R.string.http_exception_404_code)) {
                    getString(R.string.reset)
                } else {
                    getString(
                        R.string.refresh
                    )
                }
            tvStoreErrorCode.text = code
            tvStoreErrorMessage.text = message
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    companion object {
        private const val BUNDLE_KEY_SORT = "sort"
        private const val BUNDLE_KEY_CATEGORY = "category"
        private const val BUNDLE_KEY_LOWEST = "lowest"
        private const val BUNDLE_KEY_HIGHEST = "highest"
        private const val BUNDLE_KEY_SEARCH = "search"
        private const val REQUEST_KEY_BOTTOM_SHEET = "filter_data_bs"
        private const val REQUEST_KEY_SEARCH_DIALOG = "filter_data_dialog"
        private const val TAG_BOTTOM_SHEET = "Bottom sheet dialog"
        private const val PAGING_PAGE_LIMIT = 10
        private const val PAGING_PAGE = 1
    }
}
