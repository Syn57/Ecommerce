package com.luthfi.ecommerce.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.luthfi.ecommerce.MainActivity
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import com.luthfi.ecommerce.core.data.datasource.room.ProductDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

abstract class BaseFragment : Fragment() {

    private val preferences: UserPreferences by inject()
    private val database: ProductDatabase by inject()

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
    }
}
