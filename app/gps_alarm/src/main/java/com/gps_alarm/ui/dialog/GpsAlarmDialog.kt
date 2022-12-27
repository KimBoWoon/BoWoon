package com.gps_alarm.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun GpsAlarmDialog(
    message: String,
    confirmText: String,
    confirmCallback: (() -> Unit)? = null,
    dismissText: String? = null,
    dismissCallback: (() -> Unit)? = null
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = true },
            properties = DialogProperties(false, false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize()
                                .padding(vertical = 20.dp, horizontal = 10.dp),
                            text = message
                        )
                    }

                    Box {
                        Row {
                            if (!dismissText.isNullOrEmpty()) {
                                Button(
                                    shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 8.dp),
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    onClick = {
                                        dismissCallback?.invoke()
                                        showDialog = false
                                    }
                                ) {
                                    Text(text = dismissText)
                                }
                            }
                            Button(
                                shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, if (!dismissText.isNullOrEmpty()) 0.dp else 8.dp),
                                modifier = Modifier.weight(1f).height(50.dp),
                                onClick = {
                                    confirmCallback?.invoke()
                                    showDialog = false
                                }
                            ) {
                                Text(text = confirmText)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    GpsAlarmDialog(message = "", confirmText = "ㅁㄴㅇㄹ", dismissText = "")
}