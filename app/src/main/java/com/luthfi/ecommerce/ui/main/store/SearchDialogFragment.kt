package com.luthfi.ecommerce.ui.main.store

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.luthfi.ecommerce.MainActivity
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import com.luthfi.ecommerce.core.data.datasource.room.ProductDatabase
import com.luthfi.ecommerce.databinding.FragmentStoreSearchDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchDialogFragment : DialogFragment() {

    private var _binding: FragmentStoreSearchDialogBinding? = null
    private val binding get() = _binding!!
    private val analytics: FirebaseAnalytics by inject()
    private val viewModel: SearchDialogViewModel by viewModel()
    private val preferences: UserPreferences by inject()
    private val database: ProductDatabase by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreSearchDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences.getIsLogin()
            .asLiveData()
            .observe(viewLifecycleOwner) {
                if (!it) {
                    val intent = Intent((requireActivity()), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    viewLifecycleOwner.lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            database.clearAllTables()
                        }
                    }
                }
            }

        // Activate the search layout when first opened
        binding.edtSearchDialog.isFocusable = true
        binding.edtSearchDialog.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.edtSearchDialog, InputMethodManager.SHOW_IMPLICIT)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dismiss()
                }
            }
        )
        viewModel.search = arguments?.getString(BUNDLE_KEY_SEARCH)
        binding.edtSearchDialog.setText(viewModel.search)
        binding.edtSearchDialog.setSelection(viewModel.search?.length ?: 0)
        binding.edtSearchDialog.setOnEditorActionListener(
            TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val search =
                        if (binding.edtSearchDialog.text.isNullOrEmpty()) null else binding.edtSearchDialog.text.toString()
                    requireActivity().supportFragmentManager.setFragmentResult(
                        REQUEST_KEY_SEARCH_DIALOG,
                        bundleOf(
                            BUNDLE_KEY_SEARCH to search
                        )
                    )
                    analytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS) {
                        param(FirebaseAnalytics.Param.SEARCH_TERM, "$search")
                    }
                    dismiss()
                    return@OnEditorActionListener true
                }
                false
            }
        )

        val handler = Handler(Looper.getMainLooper())
        binding.edtSearchDialog.doOnTextChanged { text, _, _, _ ->
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                viewModel.search(text.toString())
            }, DEBOUNCE_TIME)
        }

        viewModel.searchResponse.observe(viewLifecycleOwner) { value ->
            binding.pbSearchDialog.isVisible = value is Resource.Loading
            binding.rvSearchDialog.visibility =
                if (value !is Resource.Loading) View.VISIBLE else View.INVISIBLE
            if (value != null) {
                when (value) {
                    is Resource.Success -> {
                        val adapter = SearchDialogAdapter(value.data.data)
                        binding.rvSearchDialog.layoutManager = LinearLayoutManager(requireContext())
                        adapter.setOnItemClickCallback(object :
                            SearchDialogAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: String) {
                                requireActivity().supportFragmentManager.setFragmentResult(
                                    REQUEST_KEY_SEARCH_DIALOG,
                                    bundleOf(BUNDLE_KEY_SEARCH to data)
                                )
                                analytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS) {
                                    param(FirebaseAnalytics.Param.SEARCH_TERM, data)
                                }
                                dismiss()
                            }
                        })
                        binding.rvSearchDialog.adapter = adapter
                    }

                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        Snackbar.make(binding.root, value.error, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val BUNDLE_KEY_SEARCH = "search"
        private const val REQUEST_KEY_SEARCH_DIALOG = "filter_data_dialog"
        private const val DEBOUNCE_TIME = 1000L
    }
}
