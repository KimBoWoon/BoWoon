package com.gps_alarm.ui.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gps_alarm.ui.Screen
import com.gps_alarm.ui.alarm.AlarmScreen
import com.gps_alarm.ui.alarm.CreateAlarmScreen
import com.gps_alarm.ui.util.dpToSp

@Composable
fun GpsMainCompose() {
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
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
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
            composable(Screen.Maps.route) { MapsScreen(navController) }
            composable(Screen.Setting.route) { SettingScreen(navController) }
            composable(Screen.CreateAlarm.route) { CreateAlarmScreen(navController) }
        }
    }
}

@Composable
fun MapsScreen(onNavigate: NavHostController) {
    Text(text = "Maps")
}

@Composable
fun SettingScreen(onNavigate: NavHostController) {
    Text(text = "Setting")
}

@Preview(
    name = "main screen preview",
    showBackground = true
)
@Composable
fun Preview() {
    GpsMainCompose()
}