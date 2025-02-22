package com.luthfi.ecommerce.ui.main.profile

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.gson.Gson
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.R as RAPP
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ErrorBody
import com.luthfi.ecommerce.databinding.FragmentProfileBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import com.luthfi.ecommerce.utils.getImageUri
import com.luthfi.ecommerce.utils.getSpan
import com.luthfi.ecommerce.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModel()

    private val analytics: FirebaseAnalytics by inject()
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        viewModel.currentImageUri?.let {
            binding.ivProfileContentPicture.visibility = View.VISIBLE
            Glide.with(this)
                .load(viewModel.currentImageUri)
                .circleCrop()
                .into(binding.ivProfileContentPicture)
//            binding.ivProfileContentPicture.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Spannable
        binding.tvProfileTnc.text = getSpan(
            requireContext(),
            resources.getString(R.string.tnc),
            arrayOf(resources.getString(R.string.term), resources.getString(R.string.condition))
        )

        // Edit text behavior
        binding.btnProfileDone.isEnabled = false
        binding.tilProfileName.editText?.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.tilProfileName.error = null
                binding.btnProfileDone.isEnabled = false
            } else if (text.toString() == "") {
                binding.btnProfileDone.isEnabled = false
                binding.tilProfileName.error = getString(R.string.please_input_your_name)
            } else {
                binding.tilProfileName.error = null
                binding.btnProfileDone.isEnabled = true
            }
        }

        // Dialog
        binding.ivProfileBgPicture.setOnClickListener {
            val upOptions = arrayOf(getString(R.string.camera), getString(R.string.gallery))
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.choose_picture))
                .setItems(upOptions) { _, which ->
                    when (which) {
                        0 -> {
                            startCamera()
                        }

                        1 -> {
                            startGallery()
                        }
                    }
                }.show()
        }

        // Button configuration
        binding.btnProfileDone.setOnClickListener {
            val name =
                binding.edtProfileName.text.toString().toRequestBody("text/plain".toMediaType())
            viewModel.currentImageUri?.let {
                val myImage = uriToFile(it, requireContext())
                val requestImage = myImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imgMultipart = MultipartBody.Part.createFormData(
                    "userImage",
                    myImage.name,
                    requestImage
                )
                viewModel.profile(name, imgMultipart)
            }
            if (viewModel.currentImageUri == null) {
                viewModel.profile(name, null)
            }
            analytics.logEvent("button_click") {
                param("button_name", "btn_profile_done")
            }
        }

        // Response observe
        viewModel.profileResponse.observe(viewLifecycleOwner) { value ->
            if (value != null) {
                binding.pbProfile.isVisible = value is Resource.Loading
                binding.btnProfileDone.visibility =
                    if (value !is Resource.Loading) View.VISIBLE else View.INVISIBLE
                when (value) {
                    is Resource.Success -> {
                        viewModel.prefSetUsername(value.data.data?.userName ?: "")
                        findNavController().navigate(RAPP.id.action_profileFragment_to_mainFragment)
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
                    else -> {}
                }
            }
        }
    }

    private fun startCamera() {
        viewModel.currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(viewModel.currentImageUri)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
