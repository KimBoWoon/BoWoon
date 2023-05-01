package com.gps_alarm.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.gps_alarm.data.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class BaseVM @Inject constructor(

) : ViewModel(), LifecycleObserver {}