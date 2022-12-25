package com.gps_alarm.ui.activities

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.gps_alarm.base.BaseActivity
import com.gps_alarm.ui.main.GpsMainCompose
import com.gps_alarm.ui.theme.GpsAlarmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GpsAlarmTheme {
                Surface {
                    GpsMainCompose()
                }
            }
        }

        requirePermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))

//        WebView.setWebContentsDebuggingEnabled(true)
    }

    override fun permissionGranted(requestCode: Int) {
    }

    override fun permissionDenied(requestCode: Int) {
//        GpsAlarmDialog("권한설정을 위해 환경설정으로 이동하시겠습니까?", "이동", {
//            Log.d(TAG, "권한 확인을 위해 환경설정 이동")
//            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).run {
//                data = Uri.parse("package:${packageName}")
//                addCategory(Intent.CATEGORY_DEFAULT)
//                startActivity(this)
//            }
//        }, "취소", { Log.d(TAG, "환경설정으로 이동 안함") }).show(supportFragmentManager, TAG)
    }
}