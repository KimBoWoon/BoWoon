package com.bowoon.gps_alarm.ui.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bowoon.gpsAlarm.databinding.AlarmFragmentBinding
import com.bowoon.gps_alarm.ui.viewmodel.AlarmVM
import com.data.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlarmFragment : Fragment() {
    companion object {
        private const val TAG = "#AlarmFragment"
    }
    private lateinit var binding: AlarmFragmentBinding
    private val viewModel by viewModels<AlarmVM>()
    private val alarmAdapter = AlarmAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = AlarmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@AlarmFragment
        }

        initBinding()
        initFlow()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAlarmList()
    }

    private fun initBinding() {
        binding.apply {
            srlAlarmFragmentRoot.setOnRefreshListener {
                srlAlarmFragmentRoot.isRefreshing = true
                viewModel.fetchAlarmList()
            }
            rvGpsAlarmList.apply {
                adapter = alarmAdapter
            }
        }
    }

    private fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.container.stateFlow.collectLatest {
                    when {
                        it.loading -> {
                            Log.d(TAG, "alarm list geocode data loading...")
                            binding.pbLoading.isVisible = true
                            binding.srlAlarmFragmentRoot.isRefreshing = true
                        }
                        it.alarmList?.isEmpty() == true && it.error == null -> {
                            Log.d(TAG, "alarm list geocode data empty")
                            binding.pbLoading.isVisible = false
                            binding.srlAlarmFragmentRoot.isRefreshing = false
                        }
                        it.alarmList?.isNotEmpty() == true && it.error == null -> {
                            Log.d(TAG, "alarm list geocode data success > ${it.alarmList}")
                            binding.pbLoading.isVisible = false
                            binding.srlAlarmFragmentRoot.isRefreshing = false
                            alarmAdapter.submitList(it.alarmList)
                        }
                        it.error != null -> {
                            Log.d(TAG, "alarm list geocode data error")
                            binding.pbLoading.isVisible = false
                            binding.srlAlarmFragmentRoot.isRefreshing = false
                        }
                        else -> {
                            Log.d(TAG, "alarm list geocode data unknown")
                            binding.pbLoading.isVisible = false
                            binding.srlAlarmFragmentRoot.isRefreshing = false
                        }
                    }
                }
            }
        }
    }
}