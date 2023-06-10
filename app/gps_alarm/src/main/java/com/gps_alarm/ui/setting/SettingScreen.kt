package com.gps_alarm.ui.setting

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.gps_alarm.ui.viewmodel.SettingVM
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(onNavigate: NavHostController) {
    SetSettingSideEffect()
    InitSettingScreen()
}

@Composable
fun SetSettingSideEffect() {
    val viewModel = hiltViewModel<SettingVM>()
    val lifecycle = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect("SettingSideEffect") {
        lifecycle.lifecycleScope.launch {
            lifecycle.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.container.sideEffectFlow.collect {
                    when (it) {
                        is SettingVM.SettingSideEffect.Save -> {
                            viewModel.setDataStore(it.setting)
                        }
                        is SettingVM.SettingSideEffect.ShowToast -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InitSettingScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SetFollowing()
        SetCircleView()
    }
}

@Composable
fun SetFollowing() {
    val viewModel = hiltViewModel<SettingVM>()
    val isFollowingState = remember { mutableStateOf(viewModel.container.stateFlow.value.isFollowing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.wrapContentWidth(),
            text = "지도에서 카메라 따라가기"
        )
        Switch(
            modifier = Modifier.wrapContentWidth(),
            checked = isFollowingState.value,
            onCheckedChange = {
                isFollowingState.value = it
                viewModel.setSetting(SettingVM.Setting.IS_FOLLOW, it)
            }
        )
    }
}

@Composable
fun SetCircleView() {
    val viewModel = hiltViewModel<SettingVM>()
    var circleSize by remember { mutableStateOf(viewModel.container.stateFlow.value.circleSize.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            label = { Text(text = "접근 범위 (m)") },
            value = circleSize,
            onValueChange = {
                circleSize = it
            }
        )
        Button(
            onClick = {
                viewModel.setSetting(SettingVM.Setting.CIRCLE_SIZE, circleSize.toInt())
            }
        ) {
            Text(text = "저장")
        }
    }
}

@Preview
@Composable
fun Preview() {
    SetFollowing()
}