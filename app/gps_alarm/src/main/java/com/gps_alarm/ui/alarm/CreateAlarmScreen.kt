package com.gps_alarm.ui.alarm

import android.graphics.Bitmap
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.web.*
import com.gps_alarm.javascript.GpsAlarmInterface
import com.gps_alarm.javascript.GpsAlarmInterfaceImpl
import com.gps_alarm.ui.fragments.AlarmVM
import util.Log

@Composable
fun CreateAlarmScreen(onNavigate: NavHostController) {
    CreateAlarmCompose()
}

@Composable
fun CreateAlarmCompose() {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = {
        showDialog = true
    }) {
        Text(text = "주소찾기")
    }

    if (showDialog) {
        AddressDialog({ showDialog = false })
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
    val url by remember { mutableStateOf("http://10.0.2.2/address.html") }
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
                it.addJavascriptInterface(GpsAlarmInterfaceImpl(callback), GpsAlarmInterface.INTERFACE_NAME)
            },
            onDispose = {
                it.removeJavascriptInterface(GpsAlarmInterface.INTERFACE_NAME)
            }
        )
    }
}