package com.bowoon.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Timer {
    private var timerJob: Job? = Job()
    private var start = 0
    val timerFlow = MutableStateFlow<TimerStatus>(TimerStatus.UnInitialized)

    fun startPeriodTimer(period: Long, action: () -> Unit) {
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                action.invoke()
                delay(period)
            }
        }
        timerFlow.value = TimerStatus.Start
    }

    fun startTimer(afterTime: Long, action: () -> Unit) {
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (start < afterTime) {
                delay(1000)
                start += 1000
            }
            action.invoke()
        }
    }

    fun stopTimer() {
        timerJob?.let {
            if (it.isActive) {
                it.cancel()
            }
            timerJob = null
        }
        timerFlow.value = TimerStatus.Stop
    }

    fun pauseTimer() {
        timerFlow.value = TimerStatus.Pause
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