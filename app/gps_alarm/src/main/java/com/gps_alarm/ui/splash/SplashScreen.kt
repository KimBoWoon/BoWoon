//package com.gps_alarm.ui.splash
//
//import android.content.Intent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import com.airbnb.lottie.compose.*
//import com.bowoon.android.gps_alarm.R
//import com.gps_alarm.ui.activities.GpsAlarmActivity
//
//@Composable
//fun SplashScreen() {
//    val context = LocalContext.current
//    val spec = LottieCompositionSpec.RawRes(R.raw.location_lottie)
//    val composition by rememberLottieComposition(spec)
//    val compositionResult: LottieCompositionResult = rememberLottieComposition(spec)
//    val lottieAnimatable = rememberLottieAnimatable()
//
//    LaunchedEffect(composition) {
//        lottieAnimatable.animate(
//            composition = composition,
//            clipSpec = LottieClipSpec.Frame(0, 1200),
//            initialProgress = 0f
//        )
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .background(color = Color.White),
//        content = {
//            LottieAnimation(
//                modifier = Modifier.width(200.dp).height(200.dp).align(Alignment.Center),
//                composition = composition,
//                progress = { lottieAnimatable.progress },
//                contentScale = ContentScale.FillHeight
//            )
//
//            if (compositionResult.isSuccess) {
//                context.startActivity(Intent(context, GpsAlarmActivity::class.java))
//            }
//        }
//    )
//}