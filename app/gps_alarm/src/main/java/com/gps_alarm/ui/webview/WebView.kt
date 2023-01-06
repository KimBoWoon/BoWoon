package com.gps_alarm.ui.webview

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.*
import com.gps_alarm.javascript.GpsAlarmInterface
import com.gps_alarm.javascript.GpsAlarmInterfaceImpl
import com.gps_alarm.ui.viewmodel.AlarmVM
import util.Log

@Composable
fun ShowWebView(
    url: String,
    dismissDialogCallback: () -> Unit,
    viewModel: AlarmVM = hiltViewModel()
) {
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