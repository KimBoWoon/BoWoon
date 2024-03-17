package com.bowoon.timer

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Timer {
    companion object {
        private const val TAG = "feat_module_timer"
    }
    private var timerJob: Job? = null
    private var period: Long = 1
    val timerFlow = MutableStateFlow<TimerStatus>(TimerStatus.UnInitialized)

    fun ready(period: Long) {
        timerFlow.value = TimerStatus.Ready
        this@Timer.period = period
    }

    fun start(action: () -> Unit) {
        if (timerJob == null) {
            timerJob = CoroutineScope(Dispatchers.IO).launch(start = CoroutineStart.LAZY) {
                while (timerFlow.value == TimerStatus.Start) {
                    action.invoke()
                    delay(period)
                }
            }
        }
        timerFlow.value = TimerStatus.Start
        timerJob?.start()
    }

    fun pause() {
        timerJob?.let {
            if (it.isActive && timerFlow.value == TimerStatus.Start) {
                timerFlow.value = TimerStatus.Pause
            }
        }
    }

    fun stop() {
        if (timerFlow.value == TimerStatus.Start) {
            timerJob?.let {
                if (it.isActive) {
                    it.cancel()
                }
                timerJob = null
            }
            timerFlow.value = TimerStatus.Stop
        } else {
            Log.d(TAG, "timer status is not start...")
        }
    }
}

sealed class TimerStatus {
    data object UnInitialized: TimerStatus()
    data object Ready : TimerStatus()
    data object Start : TimerStatus()
    data object Stop : TimerStatus()
    data object Pause : TimerStatus()
    data object Finish : TimerStatus()
}