package com.luthfi.ecommerce.ui.main.store

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.luthfi.ecommerce.databinding.FragmentSearchBottomSheetBinding
import com.luthfi.ecommerce.utils.formatBottomSheet
import org.koin.android.ext.android.inject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class SearchBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSearchBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchBottomSheetViewModel by viewModels()

    private val analytics: FirebaseAnalytics by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contextThemeWrapper =
            ContextThemeWrapper(requireActivity(), com.luthfi.ecommerce.R.style.Theme_Ecommerce)
        _binding = FragmentSearchBottomSheetBinding.inflate(
            inflater.cloneInContext(contextThemeWrapper),
            container,
            false
        )
        return binding.root
    }

    private fun onTextChangedListener(editText: TextInputEditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                editText.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    val longval: Long = originalString.toLong()
                    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)

                    //setting text after format to EditText
                    editText.setText(formattedString)
                    editText.setSelection(editText.text?.length ?: 0 )
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                editText.addTextChangedListener(this)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sort = arguments?.getString(BUNDLE_KEY_SORT)
        viewModel.category = arguments?.getString(BUNDLE_KEY_CATEGORY)
        viewModel.lowest =
            if (arguments?.getString(BUNDLE_KEY_LOWEST) != "null") {
                arguments?.getString(
                    BUNDLE_KEY_LOWEST
                )
            } else {
                null
            }
        viewModel.highest =
            if (arguments?.getString(BUNDLE_KEY_HIGHEST) != "null") {
                arguments?.getString(
                    BUNDLE_KEY_HIGHEST
                )
            } else {
                null
            }
        Log.d(
            "TAG",
            "onViewCreated: ${viewModel.sort}, ${viewModel.highest} ${viewModel.category} ${viewModel.lowest}"
        )
        resetButtonVisibility()
        when (viewModel.sort) {
            resources.getString(com.luthfi.ecommerce.R.string.rating) -> {
                binding.chipStoreRating.isChecked = true
            }

            resources.getString(com.luthfi.ecommerce.R.string.sale) -> {
                binding.chipStoreSale.isChecked = true
            }

            resources.getString(com.luthfi.ecommerce.R.string.lowest_price) -> {
                binding.chipStoreLowest.isChecked = true
            }

            resources.getString(com.luthfi.ecommerce.R.string.highest_price) -> {
                binding.chipStoreHighest.isChecked = true
            }
        }
        when (viewModel.category) {
            resources.getString(com.luthfi.ecommerce.R.string.apple) -> {
                binding.chipStoreApple.isChecked = true
            }

            resources.getString(com.luthfi.ecommerce.R.string.asus) -> {
                binding.chipStoreAsus.isChecked = true
            }

            resources.getString(com.luthfi.ecommerce.R.string.dell) -> {
                binding.chipStoreDell.isChecked = true
            }

            resources.getString(com.luthfi.ecommerce.R.string.lenovo) -> {
                binding.chipStoreLenovo.isChecked = true
            }
        }
        binding.edtSearchBsLowest.setText(
            if (viewModel.lowest != "null" && viewModel.lowest != null) {
                val originalString = viewModel.lowest.toString()
//                if (originalString.contains(",")) {
//                    originalString = originalString.replace(",".toRegex(), "")
//                }
//                val longval: Long = originalString.toLong()
//                val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
//                formatter.applyPattern("#,###,###,###")
//                val formattedString = formatter.format(longval)
//                binding.edtSearchBsLowest.setSelection(binding.edtSearchBsLowest.text?.length ?: 0 )
//                formattedString
                formatBottomSheet(originalString)
            } else {
                null
            }
        )

        binding.edtSearchBsHighest.setText(
            if (viewModel.highest != "null" && viewModel.highest != null) {
                val originalString = viewModel.highest.toString()
//                if (originalString.contains(",")) {
//                    originalString = originalString.replace(",".toRegex(), "")
//                }
//                val longval: Long = originalString.toLong()
//                val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
//                formatter.applyPattern("#,###,###,###")
//                val formattedString = formatter.format(longval)
                formatBottomSheet(originalString)
            } else {
                null
            }
        )
        binding.edtSearchBsHighest.setSelection(binding.edtSearchBsHighest.text?.length ?: 0 )

        binding.cgSearchBsSort.setOnCheckedStateChangeListener { _, _ ->
            viewModel.sort = when (binding.cgSearchBsSort.checkedChipId) {
                com.luthfi.ecommerce.R.id.chip_store_rating -> {
                    resources.getString(com.luthfi.ecommerce.R.string.rating)
                }

                com.luthfi.ecommerce.R.id.chip_store_sale -> {
                    resources.getString(com.luthfi.ecommerce.R.string.sale)
                }

                com.luthfi.ecommerce.R.id.chip_store_lowest -> {
                    resources.getString(com.luthfi.ecommerce.R.string.lowest_price)
                }

                com.luthfi.ecommerce.R.id.chip_store_highest -> {
                    resources.getString(com.luthfi.ecommerce.R.string.highest_price)
                }

                else -> {
                    null
                }
            }
            resetButtonVisibility()
        }

        binding.cgSearchBsCategory.setOnCheckedStateChangeListener { _, _ ->
            viewModel.category = when (binding.cgSearchBsCategory.checkedChipId) {
                com.luthfi.ecommerce.R.id.chip_store_apple -> {
                    resources.getString(com.luthfi.ecommerce.R.string.apple)
                }

                com.luthfi.ecommerce.R.id.chip_store_asus -> {
                    resources.getString(com.luthfi.ecommerce.R.string.asus)
                }

                com.luthfi.ecommerce.R.id.chip_store_dell -> {
                    resources.getString(com.luthfi.ecommerce.R.string.dell)
                }

                com.luthfi.ecommerce.R.id.chip_store_lenovo -> {
                    resources.getString(com.luthfi.ecommerce.R.string.lenovo)
                }

                else -> {
                    null
                }
            }
            resetButtonVisibility()
        }
        binding.edtSearchBsLowest.doOnTextChanged { _, _, _, _ ->
            if (binding.edtSearchBsLowest.text.isNullOrEmpty()){
                viewModel.lowest = null
            } else {
                var originalString = binding.edtSearchBsLowest.text.toString()
                if (originalString.contains(",")) {
                    originalString = originalString.replace(",".toRegex(), "")
                }
                viewModel.lowest = originalString
            }
//            viewModel.lowest =
//                if (binding.edtSearchBsLowest.text.isNullOrEmpty()) null else binding.edtSearchBsLowest.text.toString()
            resetButtonVisibility()
        }

        binding.edtSearchBsLowest.addTextChangedListener(onTextChangedListener(binding.edtSearchBsLowest))

        binding.edtSearchBsHighest.doOnTextChanged { _, _, _, _ ->
            if (binding.edtSearchBsHighest.text.isNullOrEmpty()){
                viewModel.highest = null
            } else {
                var originalString = binding.edtSearchBsHighest.text.toString()
                if (originalString.contains(",")) {
                    originalString = originalString.replace(",".toRegex(), "")
                }
                viewModel.highest = originalString
            }
//            viewModel.highest =
//                if (binding.edtSearchBsHighest.text.isNullOrEmpty()) null else binding.edtSearchBsHighest.text.toString()
            resetButtonVisibility()
        }

        binding.edtSearchBsHighest.addTextChangedListener(onTextChangedListener(binding.edtSearchBsHighest))

        binding.btnSearchBsShowProduct.setOnClickListener {
            setFragmentResult(
                REQUEST_KEY_BOTTOM_SHEET,
                bundleOf(
                    BUNDLE_KEY_SORT to viewModel.sort,
                    BUNDLE_KEY_CATEGORY to viewModel.category,
                    BUNDLE_KEY_LOWEST to viewModel.lowest,
                    BUNDLE_KEY_HIGHEST to viewModel.highest
                )
            )
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                val logSort = bundleOf(
                    FirebaseAnalytics.Param.ITEM_NAME to "${viewModel.sort}",
                    FirebaseAnalytics.Param.ITEM_CATEGORY to "sort"
                )
                val logBrand = bundleOf(
                    FirebaseAnalytics.Param.ITEM_NAME to "${viewModel.category}",
                    FirebaseAnalytics.Param.ITEM_CATEGORY to "brand"
                )
                val logLowest = bundleOf(
                    FirebaseAnalytics.Param.ITEM_NAME to "${viewModel.lowest}",
                    FirebaseAnalytics.Param.ITEM_CATEGORY to "lowest_price"
                )
                val logHighest = bundleOf(
                    FirebaseAnalytics.Param.ITEM_NAME to "${viewModel.highest}",
                    FirebaseAnalytics.Param.ITEM_CATEGORY to "highest_price"
                )
                param(
                    FirebaseAnalytics.Param.ITEMS,
                    arrayOf(logSort, logBrand, logLowest, logHighest)
                )
            }
            dismiss()
        }
        binding.btnSearchBsReset.setOnClickListener {
            binding.cgSearchBsSort.clearCheck()
            binding.cgSearchBsCategory.clearCheck()
            binding.edtSearchBsLowest.text?.clear()
            binding.edtSearchBsHighest.text?.clear()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        if (bottomSheetDialog is BottomSheetDialog) {
            bottomSheetDialog.behavior.skipCollapsed = true
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    private fun resetButtonVisibility() {
        binding.btnSearchBsReset.visibility =
            if (viewModel.sort != null || viewModel.category != null || viewModel.lowest != null || viewModel.highest != null) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val BUNDLE_KEY_SORT = "sort"
        private const val BUNDLE_KEY_CATEGORY = "category"
        private const val BUNDLE_KEY_LOWEST = "lowest"
        private const val BUNDLE_KEY_HIGHEST = "highest"
        private const val REQUEST_KEY_BOTTOM_SHEET = "filter_data_bs"
    }
}
