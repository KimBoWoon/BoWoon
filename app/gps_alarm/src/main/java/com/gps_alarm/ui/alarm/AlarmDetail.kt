package com.gps_alarm.ui.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gps_alarm.data.Address
import com.gps_alarm.ui.util.FixedMarkerMap
import com.gps_alarm.ui.viewmodel.AlarmVM

@Composable
fun AlarmDetailScreen(
    onNavigate: NavHostController,
    longitude: String,
    latitude: String
) {
    AlarmSideEffect(onNavigate)
    val viewModel = hiltViewModel<AlarmVM>()
    val address = viewModel.findAddress.collectAsState()

    if (longitude.isNotEmpty() && latitude.isNotEmpty()) {
        viewModel.getAddress(longitude, latitude)
    }

    if (address.value != null) {
        AlarmDetailCompose(onNavigate, address.value)
    }
}

@Composable
fun AlarmDetailCompose(
    onNavigate: NavHostController,
    address: Address?
) {
    val viewModel: AlarmVM = hiltViewModel()

    address?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            AlarmName(address = it)
            JibunAddress(address = it)
            RoadAddress(address = it)
            AlarmSetting(address = it)
            WeekRow(address = it)
            FixedMarkerMap(address = it)

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
                        address.changeWeekList(viewModel.week.toList())
                        viewModel.modifyAddress(address)
                        onNavigate.popBackStack()
                    }
                ) {
                    Text(text = "저장")
                }
                Button(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(start = 5.dp, end = 10.dp),
                    onClick = {
                        onNavigate.popBackStack()
                    }
                ) {
                    Text(text = "취소")
                }
            }
        }
    }
}

@Composable
fun AlarmName(address: Address) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "알람 이름")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "${address.name}")
    }
}

@Composable
fun JibunAddress(address: Address) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "지번 주소")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = address.jibunAddress.orEmpty())
    }
}

@Composable
fun RoadAddress(address: Address) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "도로명 주소")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = address.roadAddress.orEmpty())
    }
}

@Composable
fun AlarmSetting(
    address: Address,
    viewModel: AlarmVM = hiltViewModel()
) {
    var checkedState by remember { mutableStateOf(address.isEnable ?: false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "알림 설정")
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checkedState,
            onCheckedChange = {
                checkedState = it
                address.isEnable = it
                viewModel.modifyAddress(address)
            }
        )
    }
}

@Composable
fun WeekRow(
    address: Address
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        val weekString = listOf("일", "월", "화", "수", "목", "금", "토")
        val viewModel: AlarmVM = hiltViewModel()

        repeat(7) {
            var enabled by remember { mutableStateOf(false) }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .weight(1f)
                    .background(
                        if (enabled || address.weekList.contains(weekString[it])) {
                            Color.Green
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable {
                        if (viewModel.week.contains(weekString[it])) {
                            viewModel.week.remove(weekString[it])
                            enabled = false
                        } else {
                            viewModel.week.add(weekString[it])
                            enabled = true
                        }
                    },
                text = weekString[it],
                color = if (weekString[it] == "일") Color.Red else if (weekString[it] == "토") Color.Blue else Color.Unspecified,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}

//@Preview(
//    name = "alarmDetailPreview",
//    showSystemUi = true,
//    uiMode =  Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun AlarmDetailPreview() {
//    AlarmDetailCompose(address = Address(
//        "Address",
//        false,
//        0.0,
//        "Hello, World",
//        "jibun Address",
//        "road Address",
//        0.0,
//        0.0
//    ))
//}
//
//@Preview(
//    name = "alarmDetailPreview",
//    showSystemUi = true,
//    uiMode =  Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun AlarmDetailPreviewDarkMode() {
//    AlarmDetailCompose(address = Address(
//        "Address",
//        false,
//        0.0,
//        "Hello, World",
//        "jibun Address",
//        "road Address",
//        0.0,
//        0.0
//    ))
//}