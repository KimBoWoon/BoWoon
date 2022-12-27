package com.gps_alarm.ui.util

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
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
    message: String,
    actionLabel: String
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        coroutineScope.launch {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> Log.d("SnackbarResult Dismissed")
                SnackbarResult.ActionPerformed -> Log.d("SnackbarResult ActionPerformed")
            }
        }
    }
}