package com.gps_alarm.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseVM @Inject constructor(

) : ViewModel(), DefaultLifecycleObserver {}