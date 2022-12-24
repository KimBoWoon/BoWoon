package com.gps_alarm.ui.fragments

import android.os.Bundle
import android.view.View
import com.bowoon.android.gps_alarm.R
import com.bowoon.android.gps_alarm.databinding.FragmentSettingBinding
import com.gps_alarm.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
    }

    override fun initFlow() {
    }
}