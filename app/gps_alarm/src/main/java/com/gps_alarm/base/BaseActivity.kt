package com.gps_alarm.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper

abstract class BaseActivity : ComponentActivity() {
    companion object {
        private const val TAG = "BaseActivity"
    }

//    private val viewModel by viewModels<BaseVM>()
//    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//        permissions.keys.forEach {
//            if (permissions[it] == true) {
//                Log.d("permission granted")
//                viewModel.permissionState.value = State.Granted
//            } else if (permissions[it] == false) {
//                Log.d("permission denied")
//                viewModel.permissionState.value = State.Denied
//            } else {
//                Log.d("permission null")
//            }
//        }
//    }
//    private val dialog by lazy {
//        AlertDialog.Builder(this)
//            .setTitle("권한 확인")
//            .setMessage("앱을 사용하기 위해 권한이 필요합니다.")
//            .setNegativeButton("거부") { _, _ -> }
//            .setPositiveButton("설정으로 이동") { _, _ ->
//                Log.d("권한 확인을 위해 환경설정 이동")
//                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).run {
//                    data = Uri.parse("package:${packageName}")
//                    addCategory(Intent.CATEGORY_DEFAULT)
//                    startActivity(this)
//                }
//            }
//            .create()
//    }
//    private val permissions = arrayOf(
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.POST_NOTIFICATIONS
//    )

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        debugSetting()
//        permissionCheck()
//        initFlow()
    }

//    private fun initFlow() {
//        lifecycleScope.launchWhenStarted {
//            viewModel.permissionState.collect {
//                when (it) {
//                    is State.Unknown -> { requestPermissionLauncher.launch(permissions) }
//                    is State.Granted -> {}
//                    is State.Denied -> { showDialog() }
//                }
//            }
//        }
//    }
//
//    private fun debugSetting() {
//        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
//    }
//
//    private fun permissionCheck() {
//        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.POST_NOTIFICATIONS).forEach {
//            when {
//                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED -> {
//                    // You can use the API that requires the permission.
//                }
//                shouldShowRequestPermissionRationale(it) -> {
//                    // In an educational UI, explain to the user why your app requires this
//                    // permission for a specific feature to behave as expected, and what
//                    // features are disabled if it's declined. In this UI, include a
//                    // "cancel" or "no thanks" button that lets the user continue
//                    // using your app without granting the permission.
//                    showDialog()
//                }
//                else -> {
//                    // You can directly ask for the permission.
//                    // The registered ActivityResultCallback gets the result of this request.
//                    viewModel.permissionState.value = State.Denied
//                }
//            }
//        }
//    }
//
//    private fun showDialog() {
//        if (!dialog.isShowing) {
//            dialog.show()
//        }
//    }
}