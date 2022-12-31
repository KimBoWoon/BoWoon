package com.gps_alarm.ui.util

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import util.Log

@Composable
fun dpToSp(value: Dp) = with(LocalDensity.current) { value.toSp() }

@Composable
fun ShowSnackbar(
    scaffoldState: ScaffoldState,
    message: String,
    actionLabel: String = "OK",
    dismissSnackbarCallback: (() -> Unit)? = null,
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel
        )
        when (snackbarResult) {
            SnackbarResult.Dismissed -> {
                Log.d("SnackbarResult Dismissed")
                dismissSnackbarCallback?.invoke()
            }
            SnackbarResult.ActionPerformed -> {
                Log.d("SnackbarResult ActionPerformed")
                dismissSnackbarCallback?.invoke()
            }
        }
    }
}