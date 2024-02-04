package com.bowoon.gps_alarm.ui.alarm

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.AlarmFragmentBinding
import com.bowoon.gps_alarm.base.BaseFragment
import com.bowoon.gps_alarm.data.Address
import com.bowoon.gps_alarm.ui.util.setFadeAnimation
import com.bowoon.gps_alarm.ui.viewmodel.AlarmVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlarmFragment : BaseFragment() {
    companion object {
        private const val TAG = "#AlarmFragment"
    }

    private lateinit var binding: AlarmFragmentBinding
    private val viewModel by viewModels<AlarmVM>()
    private val alarmAdapter by lazy { AlarmAdapter(handler) }
    private val handler by lazy { ClickHandler() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlarmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@AlarmFragment
            handler = this@AlarmFragment.handler
        }

        initBinding()
        initFlow()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAlarmList()
    }

    override fun initBinding() {
        binding.apply {
            srlAlarmFragmentRoot.setOnRefreshListener {
                srlAlarmFragmentRoot.isRefreshing = true
                viewModel.fetchAlarmList()
            }
            rvGpsAlarmList.apply {
                adapter = alarmAdapter
                if (itemDecorationCount == 0) {
                    addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                            super.getItemOffsets(outRect, view, parent, state)

                            when (parent.getChildLayoutPosition(view)) {
                                0 -> {
                                    outRect.top = 16.dp
                                    outRect.bottom = 5.dp
                                }
                                parent.adapter?.itemCount -> {
                                    outRect.top = 5.dp
                                    outRect.bottom = 16.dp
                                }
                                else -> {
                                    outRect.top = 5.dp
                                    outRect.bottom = 5.dp
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alarmList.collectLatest {
                    when (it) {
                        is DataStatus.Loading -> {
                            binding.pbLoading.isVisible = true
                        }
                        is DataStatus.Success -> {
                            binding.pbLoading.isVisible = false
                            binding.srlAlarmFragmentRoot.isRefreshing = false
                            alarmAdapter.submitList(it.data)
                        }
                        is DataStatus.Failure -> {
                            binding.pbLoading.isVisible = false
                            binding.srlAlarmFragmentRoot.isRefreshing = false
                            Log.printStackTrace(it.throwable)
                        }
                    }
                }
            }
        }
    }

    inner class ClickHandler {
        fun createAlarm() {
            findNavController().navigate(
                R.id.createAlarmFragment,
                null,
                NavOptions.Builder()
                    .setFadeAnimation()
                    .build()
            )
        }

        fun removeAlarm(address: Address?) {
            address?.let {
                viewModel.removeAlarm(it)
                viewModel.fetchAlarmList()
            }
        }

        fun goToDetail(address: Address) {
            val action = AlarmFragmentDirections.actionAlarmFragmentToDetailAlarmFragment(address)
            findNavController().navigate(action)
        }
    }
}