package com.luthfi.ecommerce.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.databinding.FragmentMainBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalBadgeUtils
class MainFragment : BaseFragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainFragmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.prefGetAccessToken() == "") {
            findNavController().navigate(R.id.action_mainNav_to_preloginNav)
        } else if (viewModel.prefGetUsername() == "") {
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Navigation Setup
        val navHostFragment =
            this.childFragmentManager.findFragmentById(R.id.nav_host_fragment_fragment_main) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bnMain?.setupWithNavController(navController)
        binding.nrMain?.setupWithNavController(navController)
        binding.nvMain?.setupWithNavController(navController)

        val args = MainFragmentArgs.fromBundle(requireArguments()).flag

        Log.d("TAG", "onViewCreated: $args")
        if (args == FLAG_TRANSACTION) {
            binding.bnMain?.selectedItemId = R.id.transactionFragment
            binding.nrMain?.selectedItemId = R.id.transactionFragment
            binding.nvMain?.menu?.performIdentifierAction(R.id.transactionFragment, 0)
        }
        // Tittle
        binding.tbMain.title = viewModel.prefGetUsername()

        binding.tbMain.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.chart -> {
                    findNavController().navigate(R.id.action_mainFragment_to_chartFragment)
                    true
                }

                R.id.menu_options -> {
                    true
                }

                else -> {
                    findNavController().navigate(R.id.action_mainFragment_to_notificationFragment)
                    true
                }
            }
        }

        viewModel.getAllChart().observe(viewLifecycleOwner) { listChart ->
            if (listChart.isNotEmpty()) {
                val badge = BadgeDrawable.create(requireContext())
                badge.number = listChart.size
                BadgeUtils.attachBadgeDrawable(
                    badge,
                    binding.tbMain,
                    R.id.chart
                )
            }
        }

        viewModel.getAllFav().observe(viewLifecycleOwner) { listFav ->
            if (listFav.isNotEmpty()) {
                binding.bnMain?.getOrCreateBadge(R.id.wishlistFragment)?.number = listFav.size
                val badgeNr = binding.nrMain?.getOrCreateBadge(R.id.wishlistFragment)
                badgeNr?.isVisible = true
                badgeNr?.number = listFav.size
                val badgeText =
                    binding.nvMain?.menu?.findItem(R.id.wishlistFragment)?.actionView as TextView?
                badgeText?.isVisible = true
                badgeText?.text = listFav.size.toString()
            } else {
                binding.bnMain?.removeBadge(R.id.wishlistFragment)
                binding.nrMain?.removeBadge(R.id.wishlistFragment)
                val badgeText =
                    binding.nvMain?.menu?.findItem(R.id.wishlistFragment)?.actionView as TextView?
                badgeText?.isVisible = false
            }
        }

        viewModel.getPromoNotification().observe(viewLifecycleOwner) { listNotification ->
            if (listNotification.isNotEmpty()) {
                if (listNotification.any { !it.isChecked }) {
                    val badge = BadgeDrawable.create(requireContext())
                    badge.number = listNotification.filter { !it.isChecked }.size
                    BadgeUtils.attachBadgeDrawable(
                        badge,
                        binding.tbMain,
                        R.id.notification
                    )
                }
            }
        }
        val container: ViewGroup = binding.containerMainFragment

        container.addView(object : View(requireContext()) {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                super.onConfigurationChanged(newConfig)
                computeWindowSizeClasses()
            }
        })

        computeWindowSizeClasses()
    }

    private fun computeWindowSizeClasses() {
        val metrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(requireActivity())
        val width = metrics.bounds.width()
        val height = metrics.bounds.height()
        val density = resources.displayMetrics.density
        val windowSizeClass = WindowSizeClass.compute(width / density, height / density)
        // COMPACT, MEDIUM, or EXPANDED
        val widthWindowSizeClass = windowSizeClass.windowWidthSizeClass

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        this.arguments?.clear()
    }

    companion object {
        const val FLAG_TRANSACTION = "transaction"
    }
}
