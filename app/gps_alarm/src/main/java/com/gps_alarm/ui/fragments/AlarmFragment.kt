package com.gps_alarm.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bowoon.android.gps_alarm.R
import com.bowoon.android.gps_alarm.databinding.FragmentAlarmBinding
import com.gps_alarm.adapter.GeocodeAdapter
import com.gps_alarm.base.BaseFragment
import com.gps_alarm.ui.dialog.ShowAddressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log
import util.ViewAdapter.onDebounceClickListener

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(
    R.layout.fragment_alarm
) {
    private val viewModel by viewModels<AlarmVM>()
    private val geocodeAdapter = GeocodeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@AlarmFragment
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
            rvGeocodeList.apply {
                adapter = geocodeAdapter
            }
            edAddressSearch.onDebounceClickListener {
                ShowAddressDialog(
                    { address -> viewModel.getGeocode(address) }
                ).show(childFragmentManager, "showAddressDialog")
            }
            srlAlarmRoot.setOnRefreshListener {
                srlAlarmRoot.isRefreshing = true
                viewModel.setList()
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.geocodeList.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("geocode data loading...")
                    }
                    is DataStatus.Success -> {
                        geocodeAdapter.submitList(it.data)
                        binding.srlAlarmRoot.isRefreshing = false
                    }
                    is DataStatus.Failure -> {
                        Log.e("geocode data failure >>>>> ${it.throwable.message}")
                        binding.srlAlarmRoot.isRefreshing = false
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.geocode.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("geocode data loading...")
                    }
                    is DataStatus.Success -> {
                        viewModel.setDataStore(it.data?.addresses)
                        viewModel.setList()
                    }
                    is DataStatus.Failure -> {
                        Log.e("geocode data failure >>>>> ${it.throwable.message}")
                    }
                }
            }
        }
    }
}