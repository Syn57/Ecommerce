package com.luthfi.ecommerce.ui.prelogin.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.R as RAPP
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.databinding.FragmentLoginBinding
import com.luthfi.ecommerce.utils.getSpan
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModel()
    private val fcm: FirebaseMessaging by inject()
    private val analytics: FirebaseAnalytics by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Preference checking
        if (!viewModel.prefGetIsOnboard()) {
            if (findNavController().currentDestination?.id == RAPP.id.loginFragment) {
                findNavController().navigate(RAPP.id.action_loginFragment_to_onboardingFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Spannable
        binding.tvLoginTnc.text = getSpan(
            requireContext(),
            resources.getString(R.string.tnc),
            arrayOf(resources.getString(R.string.term), resources.getString(R.string.condition))
        )

        // Layout edit text behaviour
        binding.btnLoginLoghit.isEnabled = false
        binding.tilLoginEmail.editText?.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.tilLoginEmail.error = null
            } else if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                binding.btnLoginLoghit.isEnabled = false
                binding.tilLoginEmail.error = getString(R.string.email_is_not_valid)
            } else {
                binding.tilLoginEmail.error = null
                validateButton()
            }
        }
        binding.tilLoginPass.editText?.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.tilLoginPass.error = null
            } else if (text.toString().length < 8) {
                binding.tilLoginPass.error = getString(R.string.password_is_invalid)
                binding.btnLoginLoghit.isEnabled = false
            } else {
                binding.tilLoginPass.error = null
                validateButton()
            }
        }

        // Button integration
        fcm.token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = task.result
                binding.btnLoginLoghit.setOnClickListener {
                    val pass = binding.edtLoginPass.text.toString()
                    val email = binding.edtLoginEmail.text.toString()
                    viewModel.login(
                        LoginBody(
                            password = pass,
                            email = email,
                            firebaseToken = token
                        )
                    )
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.METHOD, "email&password")
                    analytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
                }
            }
        )
        binding.btnLoginReghit.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(RAPP.id.action_loginFragment_to_registerFragment)
        }

        // Response observer
        viewModel.loginResponse.observe(viewLifecycleOwner) { value ->
            binding.pbLogin.isVisible = value is Resource.Loading
            binding.btnLoginLoghit.visibility =
                if (value !is Resource.Loading) View.VISIBLE else View.INVISIBLE
            if (value != null) {
                when (value) {
                    is Resource.Success -> {
                        viewModel.prefSetAccessToken(value.data.data?.accessToken ?: "")
                        viewModel.prefSetRefreshToken(value.data.data?.refreshToken ?: "")
                        viewModel.prefSetUsername(value.data.data?.userName ?: "")
                        viewModel.prefSetIsLogin(true)
                        findNavController().navigate(RAPP.id.action_preloginNavto_mainNav)
                    }

                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        try {
                            val errMessage = Gson().fromJson(value.error, ErrorBody::class.java)
                            Snackbar.make(binding.root, errMessage.message, Snackbar.LENGTH_SHORT)
                                .show()
                        } catch (e: Exception) {
                            Snackbar.make(binding.root, value.error, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun validateButton() {
        binding.btnLoginLoghit.isEnabled =
            binding.tilLoginPass.error == null && binding.tilLoginEmail.error == null && binding.edtLoginPass.text.toString() != "" && binding.edtLoginEmail.text.toString() != ""
    }
}
