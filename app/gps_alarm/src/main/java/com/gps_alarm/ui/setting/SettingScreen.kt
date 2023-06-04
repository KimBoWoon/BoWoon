package com.gps_alarm.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.gps_alarm.ui.util.OnLifecycleEvent
import com.gps_alarm.ui.viewmodel.SettingVM
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(onNavigate: NavHostController) {
    SetSettingSideEffect()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SetFollowing()
    }
}

@Composable
fun SetSettingSideEffect() {
    val viewModel = hiltViewModel<SettingVM>()
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                owner.lifecycleScope.launch {
                    owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.container.sideEffectFlow.collect {
                            when (it) {
                                is SettingVM.SettingSideEffect.Save -> {
                                    viewModel.setDataStore(it.setting)
                                }
                            }
                        }
                    }
                }
            }
            Lifecycle.Event.ON_START -> {}
            Lifecycle.Event.ON_RESUME -> {}
            Lifecycle.Event.ON_PAUSE -> {}
            Lifecycle.Event.ON_STOP -> {}
            Lifecycle.Event.ON_DESTROY -> {}
            Lifecycle.Event.ON_ANY -> {}
        }
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
            text = "지도에서 카메라 따라가기")
        Switch(
            modifier = Modifier.wrapContentWidth(),
            checked = isFollowingState.value,
            onCheckedChange = {
                isFollowingState.value = it
                viewModel.setSetting(SettingVM.Setting.IS_FOLLOW.label, it)
            }
        )
    }
}

@Preview
@Composable
fun Preview() {
    SetFollowing()
}