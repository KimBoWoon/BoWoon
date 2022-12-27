package com.gps_alarm.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gps_alarm.ui.Screen
import com.gps_alarm.ui.alarm.AlarmDetailScreen
import com.gps_alarm.ui.alarm.AlarmScreen
import com.gps_alarm.ui.alarm.CreateAlarmScreen
import com.gps_alarm.ui.dialog.GpsAlarmDialog
import com.gps_alarm.ui.map.MapScreen
import com.gps_alarm.ui.setting.SettingScreen
import com.gps_alarm.ui.util.alarmDeepLink
import com.gps_alarm.ui.util.alarmDetailArgument
import com.gps_alarm.ui.util.dpToSp
import util.Log

@Composable
fun GpsMainCompose() {
    CheckPermission()

    Scaffold(
        topBar = { GpsAlarmActionBar() },
        content = { InitBottomNavigation() }
    )
}

@Composable
fun GpsAlarmActionBar() {
    TopAppBar(
        title = { Text(text = "GPS 알람", color = Color.White, fontSize = dpToSp(20.dp)) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
    )
}

@Composable
private fun InitBottomNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Alarm,
        Screen.Maps,
        Screen.Setting
    )

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Alarm.route, Modifier.padding(innerPadding)) {
            composable(Screen.Alarm.route) { AlarmScreen(navController) }
            composable(Screen.Maps.route) { MapScreen(navController) }
            composable(Screen.Setting.route) { SettingScreen(navController) }
            composable(Screen.CreateAlarm.route) { CreateAlarmScreen(navController) }
            composable(
                route = "${Screen.AlarmDetail.route}/{addressId}",
                arguments = alarmDetailArgument,
                deepLinks = alarmDeepLink
            ) {
                val args = it.arguments?.getInt("addressId") ?: -1
                AlarmDetailScreen(navController, args)
            }
        }
    }
}

@Composable
fun CheckPermission() {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val permissions = Manifest.permission.ACCESS_FINE_LOCATION

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { permissionGranted ->
            if (permissionGranted) {
                Log.d("permission true")
            } else {
                Log.d("permission false")
                showDialog = true
            }
        }
    )

    if (!showDialog) {
        SideEffect { launcher.launch(permissions) }
    }

    if (showDialog) {
        GpsAlarmDialog(
            message = "권한설정을 위해 환경설정으로 이동하시겠습니까?",
            confirmText = "이동",
            confirmCallback = {
                Log.d("권한 확인을 위해 환경설정 이동")
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).run {
                    data = Uri.parse("package:${context.packageName}")
                    addCategory(Intent.CATEGORY_DEFAULT)
                    context.startActivity(this)
                }
            },
            dismissText = "취소",
            dismissCallback = {
                Log.d("환경설정으로 이동 안함")
            }
        )
    }
}

@Preview(
    name = "main screen preview",
    showBackground = true
)
@Composable
fun Preview() {
    GpsMainCompose()
}