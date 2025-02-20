package com.luthfi.ecommerce.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.luthfi.ecommerce.databinding.FragmentHomeBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogoutHit.setOnClickListener {
            viewModel.prefLogout()
        }

        // Theme mode
        viewModel.prefGetIsLight().observe(viewLifecycleOwner) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _, b ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
                viewModel.prefSetIsLight(false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
                viewModel.prefSetIsLight(true)
            }
        }

        // Language
        viewModel.prefGetLocale().observe(viewLifecycleOwner) {
            binding.switchLanguage.isChecked = it == "id"
        }
        binding.switchLanguage.setOnCheckedChangeListener { _, b ->
            if (b) {
                val appLocale = LocaleListCompat.forLanguageTags(LOCALE_ID)
                AppCompatDelegate.setApplicationLocales(appLocale)
                viewModel.prefSetLocale("id")
            } else {
                viewModel.prefSetLocale("en")
                val appLocale = LocaleListCompat.forLanguageTags(LOCALE_EN)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val LOCALE_ID = "id-ID"
        const val LOCALE_EN = "en-US"
    }
}
