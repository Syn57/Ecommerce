package com.luthfi.ecommerce.ui.prelogin.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.databinding.FragmentOnboardingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private val viewModel: OnboardingViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Onboarding Image Configuration
        viewPager = binding.vp2Onboard
        val img =
            listOf(R.drawable.onboard_pict1, R.drawable.onboard_pict2, R.drawable.onboard_pict3)
        val adapter = ImageAdapter(img)
        viewPager.adapter = adapter
        val tabLayout = binding.tlOnboard
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position < img.size - 1) {
                    binding.btnOnboardNext.visibility = View.VISIBLE
                } else {
                    binding.btnOnboardNext.visibility = View.INVISIBLE
                }
            }
        })
        binding.btnOnboardNext.setOnClickListener {
            viewPager.currentItem += 1
        }

        // Button Action
        binding.btnOnboardJoin.setOnClickListener {
            viewModel.prefSetIsOnboard(true)
            findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
        }
        binding.btnOnboardSkip.setOnClickListener {
            viewModel.prefSetIsOnboard(true)
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
