package com.gps_alarm.ui.alarm

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.web.*
import com.gps_alarm.javascript.GpsAlarmInterface
import com.gps_alarm.javascript.GpsAlarmInterfaceImpl
import com.gps_alarm.ui.util.ShowSnackbar
import com.gps_alarm.ui.viewmodel.AlarmVM
import util.Log

@Composable
fun CreateAlarmScreen(onNavigate: NavHostController) {
    CreateAlarmCompose(onNavigate)
}

@Composable
fun CreateAlarmCompose(onNavigate: NavHostController, viewModel: AlarmVM = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var alarmTitle by remember { mutableStateOf("") }
    val geocode = viewModel.geocode.value

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = "알람 이름") },
                value = alarmTitle,
                onValueChange = { alarmTitle = it }
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
                onClick = {
                    showDialog = true
                }) {
                Text(text = "주소찾기")
            }
            when (geocode?.status) {
                "OK" -> {
                    geocode.addresses?.let {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 10.dp)
                        ) {
                            itemsIndexed(it) { index, address ->
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(horizontal = 10.dp),
                                    text = address.jibunAddress ?: ""
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(horizontal = 10.dp),
                                    text = address.roadAddress ?: ""
                                )
                            }
                        }
                    }
                }
                "INVALID_REQUEST", "SYSTEM_ERROR" -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 10.dp),
                        text = geocode.errorMessage ?: "something wrong..."
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
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.BottomStart),
        ) {
            Button(
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(start = 10.dp, end = 5.dp),
                onClick = {
                    if (!geocode?.addresses.isNullOrEmpty() && alarmTitle.isNotEmpty()) {
                        viewModel.setDataStore(alarmTitle, geocode?.addresses)
                        onNavigate.navigateUp()
                    } else {
                        showSnackbar = true
                    }
                }
            ) {
                Text(text = "저장")
            }
            Button(
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(start = 5.dp, end = 10.dp),
                onClick = { onNavigate.navigateUp() }
            ) {
                Text(text = "취소")
            }
        }
    }

    if (showDialog) {
        AddressDialog(dismissDialogCallback = { showDialog = false })
    }

    if (showSnackbar) {
        ShowSnackbar(
            message = "주소가 제대로 입력되지 않았습니다.",
            dismissSnackbarCallback = { showSnackbar = false }
        )
    }
}

@Composable
fun AddressDialog(dismissDialogCallback: () -> Unit) {
    Dialog(onDismissRequest = { dismissDialogCallback.invoke() }) {
        FindAddressWebView(dismissDialogCallback)
    }
}

@Composable
fun FindAddressWebView(dismissDialogCallback: () -> Unit, viewModel: AlarmVM = hiltViewModel()) {
    val local = "http://10.0.2.2/address.html"
    val other = "http://172.30.115.190/address.html"
    val url by remember { mutableStateOf(other) }
    val webViewState = rememberWebViewState(
        url = url,
        additionalHttpHeaders = emptyMap()
    )
    val callback: (String) -> Unit = { address ->
        viewModel.getGeocode(address)
        dismissDialogCallback.invoke()
    }

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val loadingState = webViewState.loadingState
        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = loadingState.progress,
                modifier = Modifier.fillMaxWidth()
            )
        }

        val webViewClient = remember {
            object : AccompanistWebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d("compose webview loading...")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("compose webview finish!")
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    Log.d("compose webview error! > ${error?.errorCode}, ${error?.description}")
                }
            }
        }
        val chromeClient = remember {
            object : AccompanistWebChromeClient() {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = Color.White)
        ) {
            Text(
                text = "주소입력",
                modifier = Modifier
                    .wrapContentWidth()
                    .align(alignment = Alignment.Center),
                color = Color.Black
            )
            Image(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(end = 5.dp)
                    .align(alignment = Alignment.CenterEnd)
                    .clickable { dismissDialogCallback.invoke() },
                imageVector = Icons.Filled.Close,
                contentDescription = null
            )
        }
        WebView(
            state = webViewState,
            client = webViewClient,
            chromeClient = chromeClient,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onCreated = {
                it.settings.apply {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    domStorageEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    builtInZoomControls = false
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    setSupportZoom(false)
                    setSupportMultipleWindows(true)
                }
                it.addJavascriptInterface(
                    GpsAlarmInterfaceImpl(callback),
                    GpsAlarmInterface.INTERFACE_NAME
                )
            },
            onDispose = {
                it.removeJavascriptInterface(GpsAlarmInterface.INTERFACE_NAME)
            }
        )
    }
}