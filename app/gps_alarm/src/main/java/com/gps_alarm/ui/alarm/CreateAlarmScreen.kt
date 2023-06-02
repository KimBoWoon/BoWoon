package com.gps_alarm.ui.alarm

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.data.GpsAlarmConstant.EXPAND_ANIMATION_DURATION
import com.gps_alarm.data.GpsAlarmConstant.EXPANSION_TRANSITION_DURATION
import com.gps_alarm.data.GpsAlarmConstant.FIND_ADDRESS_INVALID_REQUEST
import com.gps_alarm.data.GpsAlarmConstant.FIND_ADDRESS_OK
import com.gps_alarm.data.GpsAlarmConstant.FIND_ADDRESS_SYSTEM_ERROR
import com.gps_alarm.ui.util.FixedMarkerMap
import com.gps_alarm.ui.viewmodel.AlarmVM
import com.gps_alarm.ui.webview.ShowWebView
import util.Log

@Composable
fun CreateAlarmScreen(onNavigate: NavHostController) {
    SetSideEffect(onNavigate)
    InitCreateAlarmScreen(onNavigate)
}

@Composable
fun InitCreateAlarmScreen(onNavigate: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            FindAddressView()
        }
        SaveAddressView(onNavigate = onNavigate)
    }
}

@Composable
fun FindAddressView() {
    val viewModel = hiltViewModel<AlarmVM>()
    var showDialog by remember { mutableStateOf(false) }
    var alarmTitle by remember { mutableStateOf("") }
    val geocode by viewModel.geocode.collectAsState()

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp),
        singleLine = true,
        maxLines = 1,
        label = { Text(text = "알람 이름") },
        value = alarmTitle,
        onValueChange = {
            viewModel.alarmTitle.value = it
            alarmTitle = it
        }
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp),
        onClick = {
            showDialog = true
        }) {
        Text(text = "주소 찾기")
    }
    when (geocode?.status) {
        FIND_ADDRESS_OK -> {
            if (geocode?.addresses.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 10.dp),
                    text = "해당 주소를 찾을 수 없습니다."
                )
            } else {
                FoundAddressList(geocode?.addresses)
            }
        }
        FIND_ADDRESS_INVALID_REQUEST, FIND_ADDRESS_SYSTEM_ERROR -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
                text = geocode?.errorMessage ?: "something wrong..."
            )
        }
        else -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
                text = "검색된 주소의 리스트가 나타납니다.\n주소를 검색해보세요!"
            )
        }
    }

    if (showDialog) {
        AddressDialog(dismissDialogCallback = { showDialog = false })
    }
}

@Composable
fun SaveAddressView(onNavigate: NavHostController) {
    val viewModel = hiltViewModel<AlarmVM>()
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .weight(weight = 1f)
                .padding(start = 10.dp, end = 5.dp),
            onClick = {
                if (viewModel.chooseAddress.value != null && viewModel.alarmTitle.value.isNotEmpty()) {
                    viewModel.addAlarm(viewModel.alarmTitle.value, viewModel.chooseAddress.value)
                    viewModel.chooseAddress.value = null
                    onNavigate.navigateUp()
                } else {
                    Toast.makeText(context, "주소가 제대로 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = "저장")
        }
        Button(
            modifier = Modifier
                .weight(weight = 1f)
                .padding(start = 5.dp, end = 10.dp),
            onClick = {
                viewModel.chooseAddress.value = null
                onNavigate.navigateUp()
            }
        ) {
            Text(text = "취소")
        }
    }
}

@Composable
fun FoundAddressList(addresses: List<Addresses>?) {
    val viewModel = hiltViewModel<AlarmVM>()
    val listState = rememberLazyListState()
    val lifecycle = LocalLifecycleOwner.current
    val expandedCardIds by viewModel.expandedAddressItem.collectAsState()

    addresses?.let {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = listState
        ) {
            itemsIndexed(it) { index, address ->
                ExpandableCard(
                    address = address,
                    onAddressClick = {
                        viewModel.onCardArrowClicked(index)
                        lifecycle.lifecycleScope.launchWhenStarted {
                            listState.scrollToItem(index)
                        }
                    },
                    expanded = expandedCardIds == index
                )
            }
        }
    } ?: run {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp),
            text = "해당 주소를 찾을 수 없습니다."
        )
    }
}

@Composable
fun AddressDialog(dismissDialogCallback: () -> Unit) {
    Dialog(onDismissRequest = { dismissDialogCallback.invoke() }) {
//        val local = "http://172.30.50.8/address.html"
        val local = "http://192.168.35.56/address.html"
        val viewModel = hiltViewModel<AlarmVM>()
        val executeCallback: (String) -> Unit = {
            viewModel.getGeocode(it)
            dismissDialogCallback.invoke()
        }
        ShowWebView(
            local,
            executeCallback,
            dismissDialogCallback
        )
    }
}

@Composable
fun ExpandableCard(
    address: Addresses,
    onAddressClick: () -> Unit,
    expanded: Boolean,
) {
    val viewModel = hiltViewModel<AlarmVM>()
    val chooseAddress = viewModel.chooseAddress.collectAsState()
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "")
    val arrowRotationDegree by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = EXPAND_ANIMATION_DURATION)
        },
        label = "",
        targetValueByState = {
            Log.d("targetValueByState > $it")
            if (expanded) 0f else 180f
        }
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .clickable { onAddressClick.invoke() },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chooseAddress.value?.longitude == address.longitude && chooseAddress.value?.latitude == address.latitude) {
                ChooseAddress()
            }
            CardArrow(degrees = arrowRotationDegree)
            CardTitle(title = address.jibunAddress ?: "")
        }
        ExpandableContent(visible = expanded, address = address)
    }
}

@Composable
fun ChooseAddress() {
    Icon(
        imageVector = Icons.Filled.Done,
        contentDescription = "ChooseAddress",
    )
}

@Composable
fun CardArrow(
    degrees: Float
) {
    Icon(
        imageVector = Icons.Filled.ExpandMore,
        contentDescription = "Expandable Arrow",
        modifier = Modifier.rotate(degrees),
    )
}

@Composable
fun CardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
        textAlign = TextAlign.Start,
    )
}

@Composable
fun ExpandableContent(
    visible: Boolean = true,
    address: Addresses? = null
) {
    val viewModel = hiltViewModel<AlarmVM>()
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(EXPANSION_TRANSITION_DURATION)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(EXPANSION_TRANSITION_DURATION)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top,
            animationSpec = tween(EXPANSION_TRANSITION_DURATION)
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
            animationSpec = tween(EXPANSION_TRANSITION_DURATION)
        )
    }
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 0.dp),
        enter = enterTransition,
        exit = exitTransition,
        visible = visible,
    ) {
        Column {
            FixedMarkerMap(address)
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    viewModel.chooseAddress.value = address
                    viewModel.expandedAddressItem.value = -1
                }
            ) {
                Text(text = "주소 확인")
            }
        }
    }
}