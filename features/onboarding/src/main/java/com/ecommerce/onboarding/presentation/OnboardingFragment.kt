package com.ecommerce.onboarding.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ecommerce.onboarding.R
import com.ecommerce.onboarding.databinding.FragmentOnboardingBinding
import com.ecommerce.uicomponent.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(
    FragmentOnboardingBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}