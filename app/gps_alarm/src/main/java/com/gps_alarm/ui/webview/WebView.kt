package com.gps_alarm.ui.webview

import android.webkit.WebSettings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.gps_alarm.javascript.GpsAlarmInterface
import com.gps_alarm.javascript.GpsAlarmInterfaceImpl
import com.gps_alarm.ui.webview.settings.WebViewChromeClient
import com.gps_alarm.ui.webview.settings.WebViewClient

@Composable
fun ShowWebView(
    url: String,
    executeCallback: ((String) -> Unit)? = null,
    dismissDialogCallback: (() -> Unit)? = null
) {
    val webViewState = rememberWebViewState(
        url = url,
        additionalHttpHeaders = emptyMap()
    )

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val loadingState = webViewState.loadingState
        val webViewClient = remember { WebViewClient() }
        val chromeClient = remember { WebViewChromeClient() }

        WebViewTitle(dismissDialogCallback)
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
                    allowContentAccess = true
                    allowFileAccess = true
                    setSupportZoom(false)
                    setSupportMultipleWindows(true)
                }
                it.addJavascriptInterface(
                    GpsAlarmInterfaceImpl(executeCallback),
                    GpsAlarmInterface.INTERFACE_NAME
                )
            },
            onDispose = {
                it.removeJavascriptInterface(GpsAlarmInterface.INTERFACE_NAME)
            }
        )

        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = loadingState.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ShowCustomWebView(
    url: String,
    javascriptInterface: List<Pair<GpsAlarmInterface, String>>? = null,
    dismissDialogCallback: (() -> Unit)? = null
) {
    val webViewState = rememberWebViewState(
        url = url,
        additionalHttpHeaders = emptyMap()
    )

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val loadingState = webViewState.loadingState
        val webViewClient = remember { WebViewClient() }
        val chromeClient = remember { WebViewChromeClient() }

        WebViewTitle(dismissDialogCallback)
        WebView(
            state = webViewState,
            client = webViewClient,
            chromeClient = chromeClient,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onCreated = { webView ->
                webView.settings.apply {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    domStorageEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    builtInZoomControls = false
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    allowContentAccess = true
                    allowFileAccess = true
                    setSupportZoom(false)
                    setSupportMultipleWindows(true)
                }
                javascriptInterface?.forEach { (implementClass, className) ->
                    webView.addJavascriptInterface(implementClass, className)
                }
            },
            onDispose = { webView ->
                javascriptInterface?.forEach { (_, className) ->
                    webView.removeJavascriptInterface(className)
                }
            }
        )

        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = loadingState.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun WebViewTitle(dismissDialogCallback: (() -> Unit)? = null) {
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
                .clickable { dismissDialogCallback?.invoke() },
            imageVector = Icons.Filled.Close,
            contentDescription = null
        )
    }
}