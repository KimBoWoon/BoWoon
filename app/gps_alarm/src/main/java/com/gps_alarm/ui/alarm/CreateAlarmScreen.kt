package com.gps_alarm.ui.alarm

import android.graphics.Bitmap
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.gps_alarm.ui.util.dpToSp
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
    val scaffoldState = rememberScaffoldState()
    val geocode = viewModel.geocode.value

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        topBar = { CreateAlarmTopBar() },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.Bottom
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
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
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
                Text(text = geocode?.addresses?.firstOrNull()?.jibunAddress ?: "")
                Text(text = geocode?.addresses?.firstOrNull()?.roadAddress ?: "")
            }
        }
    )

    if (showDialog) {
        AddressDialog(dismissDialogCallback = { showDialog = false })
    }

    if (showSnackbar) {
        ShowSnackbar(
            scaffoldState = scaffoldState,
            message = "주소가 제대로 입력되지 않았습니다.",
            dismissSnackbarCallback = { showSnackbar = false }
        )
    }
}

@Composable
fun CreateAlarmTopBar() {
    TopAppBar(
        title = { Text(text = "알람 추가하기", color = Color.White, fontSize = dpToSp(20.dp)) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
    )
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
    val other = "http://192.168.35.128/address.html"
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
            }
        }
        val chromeClient = remember {
            object : AccompanistWebChromeClient() {}
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