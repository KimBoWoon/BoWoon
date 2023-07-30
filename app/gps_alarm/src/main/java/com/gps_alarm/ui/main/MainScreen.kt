package com.gps_alarm.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.gps_alarm.ui.map.MapScreen
import com.gps_alarm.ui.setting.SettingScreen
import com.gps_alarm.ui.util.CheckPermission
import com.gps_alarm.ui.util.alarmDeepLink
import com.gps_alarm.ui.util.alarmDetailArgument
import com.gps_alarm.ui.util.dpToSp
import com.gps_alarm.ui.viewmodel.GpsAlarmVM
import kotlinx.coroutines.launch

@Composable
fun GpsMainCompose() {
    val navController = rememberNavController()

    MainSideEffect()

    Scaffold(
        topBar = { GpsAlarmActionBar() },
        content = { innerPadding ->InitNavHost(navController, innerPadding) },
        bottomBar = { InitBottomNavigation(navController) }
    )
}

@Composable
fun MainSideEffect() {
    val checkPermissionFlag = remember { mutableStateOf(false) }
    val viewModel = hiltViewModel<GpsAlarmVM>()
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect("MainSideEffect") {
        lifecycle.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.container.sideEffectFlow.collect {
                    when (it) {
                        GpsAlarmVM.GpsAlarmSideEffect.CheckPermission -> checkPermissionFlag.value = true
                    }
                }
            }
        }
    }

    if (checkPermissionFlag.value) {
        CheckPermission()
    }
}

@Composable
fun GpsAlarmActionBar() {
    val viewModel = hiltViewModel<GpsAlarmVM>()

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
private fun InitBottomNavigation(navController: NavHostController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        NavigationScreen.values().forEach { screen ->
            if (screen == NavigationScreen.CreateAlarm || screen == NavigationScreen.AlarmDetail) {
                return@forEach
            }
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
    val viewModel = hiltViewModel<GpsAlarmVM>()

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