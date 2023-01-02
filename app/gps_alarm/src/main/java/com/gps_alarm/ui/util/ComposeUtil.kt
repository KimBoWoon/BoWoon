package com.gps_alarm.ui.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun dpToSp(value: Dp) = with(LocalDensity.current) { value.toSp() }

@Composable
fun ShowSnackbar(
    message: String,
    actionLabel: String = "OK",
    dismissSnackbarCallback: (() -> Unit)? = null,
) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
//    val snackbarHostState = remember { SnackbarHostState() }
//    val channel = remember { Channel<Int>(Channel.Factory.CONFLATED) }
//
//    LaunchedEffect(channel) {
//        channel.receiveAsFlow().collect { index ->
//            val result = snackbarHostState.showSnackbar(
//                message = message,
//                actionLabel = actionLabel
//            )
//            when (result) {
//                SnackbarResult.ActionPerformed -> {
//                    Log.d("SnackbarResult ActionPerformed")
//                    dismissSnackbarCallback?.invoke()
//                }
//                SnackbarResult.Dismissed -> {
//                    Log.d("SnackbarResult Dismissed")
//                    dismissSnackbarCallback?.invoke()
//                }
//            }
//        }
//    }
}