package com.bowoon.lol.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.ContextUtils.showToast
import com.bowoon.lol.ui.dialog.LolDialog

abstract class BaseActivity<V : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {
    companion object {
        private const val TAG = "BaseActivity"
        const val PERMISSION_REQUEST_CODE = 1000
    }

    protected val binding: V by lazy {
        DataBindingUtil.setContentView(this, layoutId)
    }

    abstract fun initBinding()
    abstract fun initFlow()

    /**
     * 권한을 얻었을 경우
     */
    abstract fun permissionGranted(requestCode: Int)

    /**
     * 권한을 얻지 못했을 경우
     */
    abstract fun permissionDenied(requestCode: Int)

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "권한 승인")
            permissionGranted(PERMISSION_REQUEST_CODE)
        } else {
            Log.d(TAG, "권한 거부")
            permissionDenied(PERMISSION_REQUEST_CODE)
        }
    }

    fun requirePermissions(permissions: Array<String>) {
        permissions.forEach { permission ->
            when (PackageManager.PERMISSION_DENIED) {
                ContextCompat.checkSelfPermission(this, permission) -> {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        LolDialog(
                            "권한설정을 위해 환경설정으로 이동하시겠습니까?",
                            "이동",
                            {
                                Log.d(TAG, "권한 확인을 위해 환경설정 이동")
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).run {
                                    data = Uri.parse("package:${packageName}")
                                    addCategory(Intent.CATEGORY_DEFAULT)
                                    startActivity(this)
                                }
                            },
                            "취소",
                            { Log.d(TAG, "환경설정으로 이동 안함") }
                        ).show(supportFragmentManager, TAG)
                    } else {
                        ActivityCompat.requestPermissions(this@BaseActivity, permissions, PERMISSION_REQUEST_CODE)
                    }
                }
                else -> {
                    requestPermissionLauncher.launch(permission)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        grantResults.forEachIndexed { index, result ->
            if (result == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "${permissions[index]} 권한 승인")
            } else {
                Log.d(TAG, "${permissions[index]} 권한 거부")
                showToast("해당 기능을 사용하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT)
            }
        }
    }
}