package com.gps_alarm.ui.util

import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import util.Log

@Composable
fun dpToSp(value: Dp) = with(LocalDensity.current) { value.toSp() }

@Composable
fun ShowSnackbar(
    message: String,
    actionLabel: String = "OK",
    dismissSnackbarCallback: (() -> Unit)? = null,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val channel = remember { Channel<Int>(Channel.CONFLATED) }

    LaunchedEffect(channel) {
        channel.receiveAsFlow().collect { index ->
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    Log.d("SnackbarResult ActionPerformed")
                    dismissSnackbarCallback?.invoke()
                }
                SnackbarResult.Dismissed -> {
                    Log.d("SnackbarResult Dismissed")
                    dismissSnackbarCallback?.invoke()
                }
            }
        }
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}