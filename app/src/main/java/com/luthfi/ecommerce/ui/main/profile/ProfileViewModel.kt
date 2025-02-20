package com.luthfi.ecommerce.ui.main.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProfileResponse
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    private var _profileResponse: MutableLiveData<Resource<ProfileResponse>> =
        MutableLiveData<Resource<ProfileResponse>>()
    val profileResponse: LiveData<Resource<ProfileResponse>> = _profileResponse

    var currentImageUri: Uri? = null

    // Api
    fun profile(userName: RequestBody, userImage: MultipartBody.Part?) {
        viewModelScope.launch {
            _profileResponse.value = Resource.Loading
            repository.profile(userName, userImage).collect { value ->
                _profileResponse.value = value
            }
        }
    }

    // Preference
    fun prefSetUsername(value: String) {
        runBlocking {
            repository.setUsername(value)
        }
    }
}
