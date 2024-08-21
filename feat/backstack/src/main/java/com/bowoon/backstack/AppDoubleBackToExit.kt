package com.bowoon.backstack

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class AppDoubleBackToExit @AssistedInject constructor(
//    @Assisted("exitMsg") @StringRes private val exitMsg: Int,
    @Assisted("exitTime") private val exitTime: Long,
//    @ActivityContext private val context: Context
) {
    @AssistedFactory
    interface AppDoubleBackToExitFactory {
        fun create(
//            @Assisted("exitMsg") @StringRes exitMsg: Int,
            @Assisted("exitTime") exitTime: Long
        ): AppDoubleBackToExit
    }

    val event = MutableStateFlow<AppDoubleBackToExitEvent>(AppDoubleBackToExitEvent.One)
//    private var exitFlag = false
//
//    fun onBackPressed(
//        callback: () -> Unit
//    ) {
//        when (exitFlag) {
//            true -> callback.invoke()
//            false -> {
//                Toast.makeText(context, exitMsg, Toast.LENGTH_SHORT).show()
//                exitFlag = true
//                CoroutineScope(Dispatchers.Main).launch {
//                    delay(exitTime)
//                    exitFlag = false
//                }
//            }
//        }
//    }

    suspend fun onBackPressed() {
        when (event.value) {
            AppDoubleBackToExitEvent.One -> {
                event.emit(AppDoubleBackToExitEvent.Two)
                delay(exitTime)
                event.emit(AppDoubleBackToExitEvent.One)
            }
            AppDoubleBackToExitEvent.Two -> event.emit(AppDoubleBackToExitEvent.Exit)
            AppDoubleBackToExitEvent.Exit -> {}
        }
    }
}

sealed class AppDoubleBackToExitEvent {
    data object One : AppDoubleBackToExitEvent()
    data object Two : AppDoubleBackToExitEvent()
    data object Exit : AppDoubleBackToExitEvent()
}