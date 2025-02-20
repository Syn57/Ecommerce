package com.luthfi.ecommerce.ui.prelogin.register

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
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.databinding.FragmentRegisterBinding
import com.luthfi.ecommerce.utils.getSpan
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val fcm: FirebaseMessaging by inject()
    private val viewModel: RegisterViewModel by viewModel()
    private val analytics: FirebaseAnalytics by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Spannable
        binding.tvRegisterTnc.text = getSpan(
            requireContext(),
            resources.getString(R.string.tnc),
            arrayOf(resources.getString(R.string.term), resources.getString(R.string.condition))
        )

        // Edit text behaviour
        binding.btnRegisterReghit.isEnabled = false
        binding.tilRegisterEmail.editText?.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.tilRegisterEmail.error = null
            } else if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                binding.btnRegisterReghit.isEnabled = false
                binding.tilRegisterEmail.error = getString(R.string.email_is_not_valid)
            } else {
                binding.tilRegisterEmail.error = null
                validateButton()
            }
        }
        binding.tilRegisterPass.editText?.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.tilRegisterPass.error = null
            } else if (text.toString().length < 8) {
                binding.tilRegisterPass.error = getString(R.string.password_is_invalid)
                binding.btnRegisterReghit.isEnabled = false
            } else {
                binding.tilRegisterPass.error = null
                validateButton()
            }
        }

        // Button configuration
        binding.btnRegisterLoghit.setOnClickListener {
            val controller = Navigation.findNavController(view)
            controller.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        fcm.token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = task.result
                binding.btnRegisterReghit.setOnClickListener {
                    val pass = binding.edtRegisterPass.text.toString()
                    val email = binding.edtRegisterEmail.text.toString()
                    viewModel.register(
                        LoginBody(
                            password = pass,
                            email = email,
                            firebaseToken = token
                        )
                    )
                    val bundle = Bundle()
//                bundle.putString(FirebaseAnalytics.Param.METHOD, "$pass, $email")
                    bundle.putString(FirebaseAnalytics.Param.METHOD, "email/password")
                    analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
                }
            }
        )

        viewModel.registerResponse.observe(viewLifecycleOwner) { value ->
            binding.pbRegister.isVisible = value is Resource.Loading
            binding.btnRegisterReghit.visibility =
                if (value !is Resource.Loading) View.VISIBLE else View.INVISIBLE
            if (value !is Resource.Loading) View.VISIBLE else View.INVISIBLE
            if (value != null) {
                when (value) {
                    is Resource.Success -> {
                        viewModel.prefSetAccessToken(value.data.data?.accessToken ?: "")
                        viewModel.prefSetRefreshToken(value.data.data?.refreshToken ?: "")
                        viewModel.prefSetIsLogin(true)
                        findNavController().navigate(R.id.action_preloginNavto_mainNav)
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
        binding.btnRegisterReghit.isEnabled =
            binding.tilRegisterPass.error == null && binding.tilRegisterEmail.error == null && binding.edtRegisterPass.text.toString() != "" && binding.edtRegisterEmail.text.toString() != ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
