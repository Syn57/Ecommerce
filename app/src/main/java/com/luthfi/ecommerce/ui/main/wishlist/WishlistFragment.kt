package com.luthfi.ecommerce.ui.main.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.luthfi.ecommerce.MainActivity
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.databinding.FragmentWishlistBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WishlistFragment : BaseFragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WishlistViewModel by viewModel()

    private val analytics: FirebaseAnalytics by inject()

    private lateinit var adapter: WishlistListAdapter
    private lateinit var lmLinear: LinearLayoutManager
    private lateinit var lmGrid: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllFav().observe(viewLifecycleOwner) { listFav ->
            showError(listFav!!.isEmpty())
            showFavorite(listFav.isEmpty(), listFav)
            binding.tvWishlistItems.text = getString(R.string.item_s, listFav.size)
        }
        adapter = WishlistListAdapter(requireContext(), { id ->
            deleteItem(id)
        }, { item ->
            addChart(item)
        })
        lmLinear = LinearLayoutManager(requireContext())
        lmGrid = GridLayoutManager(requireContext(), SPAN_SIZE)
        binding.rvWishlist.layoutManager = if (viewModel.isGridLayout) lmGrid else lmLinear
        adapter.setLayoutType(viewModel.isGridLayout)
        adapter.setOnItemClickCallback(object : WishlistListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Product) {
                (requireActivity() as MainActivity).moveToDetail(data.productId, data.variantName)
            }
        })
        binding.rvWishlist.itemAnimator = null
        binding.rvWishlist.adapter = adapter
    }

    private fun deleteItem(id: String) {
        viewModel.deleteFav(id)
    }

    private fun addChart(chart: Product) {
        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
            val bs = Bundle()
            bs.putString(FirebaseAnalytics.Param.ITEM_NAME, chart.productName)
            bs.putString(FirebaseAnalytics.Param.ITEM_BRAND, chart.brand)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(bs))
            param(FirebaseAnalytics.Param.CURRENCY, INDONESIA_CURRENCY)
            param(
                FirebaseAnalytics.Param.VALUE,
                (chart.productPrice + chart.variantPrice).toDouble()
            )
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val message = viewModel.addChart(chart, true)
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
    }

    private fun showFavorite(state: Boolean, listFav: List<Product>) {
        if (!state) {
            adapter.submitList(listFav)
            binding.ivWishlistLayoutType.setOnClickListener {
                viewModel.isGridLayout = !viewModel.isGridLayout
                binding.ivWishlistLayoutType.setImageResource(
                    when (viewModel.isGridLayout) {
                        true -> R.drawable.ic_list_grid
                        else -> R.drawable.ic_list_linear
                    }
                )
                binding.rvWishlist.layoutManager = if (viewModel.isGridLayout) lmGrid else lmLinear
                adapter.setLayoutType(viewModel.isGridLayout)
                binding.rvWishlist.adapter = adapter
                adapter.submitList(listFav)
            }
        }
    }

    private fun showError(state: Boolean) {
        binding.ivWishlistError.isVisible = state == true
        binding.tvWishlistErrorCode.isVisible = state == true
        binding.tvWishlistErrorMessage.isVisible = state == true
        binding.tvWishlistItems.isVisible = state == false
        binding.ivWishlistLayoutType.isVisible = state == false
        binding.dvdrWishlist1.isVisible = state == false
        binding.rvWishlist.isVisible = state == false
    }

    companion object {
        const val SPAN_SIZE = 2
        const val INDONESIA_CURRENCY = "IDR"
        const val CART_ADDED = "cartAdded"
    }
}
