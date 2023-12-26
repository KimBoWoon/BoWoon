package com.bowoon.gps_alarm.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun initBinding()
    abstract fun initFlow()
}