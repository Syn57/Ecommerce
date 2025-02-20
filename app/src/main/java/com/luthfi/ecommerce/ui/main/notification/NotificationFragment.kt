package com.luthfi.ecommerce.ui.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.databinding.FragmentNotificationBinding
import com.luthfi.ecommerce.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : BaseFragment() {

    private val viewModel: NotificationViewModel by viewModel()

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotificationAdapter(requireContext()) { id, value ->
            updateIsChecked(id, value)
        }
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotification.adapter = adapter
        viewModel.getPromoNotification().observe(viewLifecycleOwner) {
            adapter.submitList(it.reversed())
            binding.ivNotificationError.isVisible = it.isNullOrEmpty()
            binding.tvNotificationErrorCode.text = getString(R.string.http_exception_404_code)
            binding.tvNotificationErrorCode.isVisible = it.isNullOrEmpty()
            binding.tvNotificationErrorMessage.isVisible = it.isNullOrEmpty()
        }

        binding.tbNotification.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateIsChecked(id: Int, state: Boolean) {
        viewModel.updateIsCheck(id, state)
    }
}
