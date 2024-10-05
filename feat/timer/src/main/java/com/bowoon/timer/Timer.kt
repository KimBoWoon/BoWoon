package com.bowoon.timer

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class Timer @Inject constructor(
    @ActivityContext private val context: Context
) {
    companion object {
        private const val TAG = "feat_module_timer"
    }

    private var timerJob: Job? = null
    private var period = 1L
    val timerFlow = MutableStateFlow<TimerEvent>(TimerEvent.UnInitialized)

    fun ready(period: Long) {
        timerFlow.value = TimerEvent.Ready
        this@Timer.period = period
    }

    fun start(period: Long, action: () -> Unit) {
        ready(period)
        start(action)
    }

    fun start(action: () -> Unit) {
        if (timerJob == null) {
            (context as? FragmentActivity)?.let {
                timerJob = it.lifecycleScope.launch(Dispatchers.IO) {
                    while (timerFlow.value == TimerEvent.Start) {
                        withContext(Dispatchers.Main) {
                            action.invoke()
                        }
                        delay(period)
                    }
                }
            }
//            timerJob = CoroutineScope(Dispatchers.IO).launch {
//                while (timerFlow.value == TimerEvent.Start) {
//                    withContext(Dispatchers.Main) {
//                        action.invoke()
//                    }
//                    delay(period)
//                }
//            }
        }
        timerFlow.value = TimerEvent.Start
    }

    fun stop() {
        if (timerFlow.value == TimerEvent.Start) {
            timerJob?.let {
                if (it.isActive) {
                    it.cancel()
                }
                timerJob = null
            }
            timerFlow.value = TimerEvent.Stop
        } else {
            Log.d(TAG, "timer status is not start...")
        }
    }
}

sealed class TimerEvent {
    data object UnInitialized : TimerEvent()
    data object Ready : TimerEvent()
    data object Start : TimerEvent()
    data object Stop : TimerEvent()
    data object Finish : TimerEvent()
}