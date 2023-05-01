package com.gps_alarm.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gps_alarm.ui.NavigationScreen
import com.gps_alarm.ui.alarm.AlarmDetailScreen
import com.gps_alarm.ui.alarm.AlarmScreen
import com.gps_alarm.ui.alarm.CreateAlarmScreen
import com.gps_alarm.ui.dialog.GpsAlarmDialog
import com.gps_alarm.ui.map.MapScreen
import com.gps_alarm.ui.setting.SettingScreen
import com.gps_alarm.ui.util.alarmDeepLink
import com.gps_alarm.ui.util.alarmDetailArgument
import com.gps_alarm.ui.util.dpToSp
import com.gps_alarm.ui.viewmodel.GpsAlarmVM
import util.Log

@Composable
fun GpsMainCompose() {
    val navController = rememberNavController()
    val items = listOf(
        NavigationScreen.Alarm,
        NavigationScreen.Maps,
        NavigationScreen.Setting
    )

    CheckPermission()

    Scaffold(
        topBar = { GpsAlarmActionBar() },
        content = { innerPadding -> InitNavHost(navController, innerPadding) },
        bottomBar = { InitBottomNavigation(items, navController) }
    )
}

@Composable
fun GpsAlarmActionBar() {
    val viewModel: GpsAlarmVM = hiltViewModel()

    TopAppBar(
        title = { Text(text = viewModel.appBarTitle.value, color = Color.White, fontSize = dpToSp(20.dp)) },
        actions = {},
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
    )
}

@Composable
private fun InitBottomNavigation(items: List<NavigationScreen>, navController: NavHostController) {
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
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun InitNavHost(navController: NavHostController, innerPadding: PaddingValues) {
    val viewModel: GpsAlarmVM = hiltViewModel()

    NavHost(navController, startDestination = NavigationScreen.Alarm.route, Modifier.padding(innerPadding)) {
        composable(NavigationScreen.Alarm.route) {
            viewModel.appBarTitle.value = "알람 리스트"
            AlarmScreen(navController)
        }
        composable(NavigationScreen.Maps.route) {
            viewModel.appBarTitle.value = "지도"
            MapScreen(navController)
        }
        composable(NavigationScreen.Setting.route) {
            viewModel.appBarTitle.value = "설정"
            SettingScreen(navController)
        }
        composable(NavigationScreen.CreateAlarm.route) {
            viewModel.appBarTitle.value = "알람 만들기"
            CreateAlarmScreen(navController)
        }
        composable(
            route = "${NavigationScreen.AlarmDetail.route}/{longitude}/{latitude}",
            arguments = alarmDetailArgument,
            deepLinks = alarmDeepLink
        ) {
            viewModel.appBarTitle.value = "알람 세부 정보"
            val longitude = it.arguments?.getString("longitude") ?: ""
            val latitude = it.arguments?.getString("latitude") ?: ""
            AlarmDetailScreen(navController, longitude, latitude)
        }
    }
}

@Composable
fun CheckPermission() {
    val permissionList = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS)
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.keys.forEach {
                if (permissions[it] == true) {
                    Log.d("permission granted")
                } else if (permissions[it] == false) {
                    Log.d("permission denied")
//                    ShowDialog()
                } else {
                    Log.d("permission null")
                }
            }
        }
    )

    val showDialogList = mutableListOf<String>()
    var permissionDenied = 0

    permissionList.forEach {
        if (shouldShowRequestPermissionRationale(context as Activity, it)) {
            showDialogList.add(it)
        } else if (ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED) {
            permissionDenied++
        }
    }

    Log.i("permission denied > ${permissionDenied}, show dialog > ${showDialogList.size}")

    when {
        showDialogList.size > 0 -> {
            //Some permissions are denied and can be asked again.
            SideEffect { launcher.launch(permissionList) }
        }
        permissionDenied > 0 -> {
            //Show alert dialog
            ShowDialog()
        }
        else -> {
            //All permissions granted. Do your stuff 🤞
            Log.d("all permission granted")
        }
    }
}

@Composable
fun ShowDialog() {
    val context = LocalContext.current

    GpsAlarmDialog(
        message = "앱을 사용하기위해 위치와 알림 권한이 필요합니다.\n권한설정을 위해 환경설정으로 이동하시겠습니까?",
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