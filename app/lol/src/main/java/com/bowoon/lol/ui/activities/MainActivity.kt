package com.bowoon.lol.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bowoon.commonutils.Log
import com.bowoon.lol.R
import com.bowoon.lol.base.BaseActivity
import com.bowoon.lol.databinding.ActivityMainBinding
import com.bowoon.lol.ui.activities.vm.MainVM
import com.bowoon.commonutils.ContextUtils.showToast
import com.bowoon.commonutils.DataStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    private val viewModel by viewModels<MainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
        initNavigation()
    }

    override fun initBinding() {
        binding.apply {}

//        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.lolVersion.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                    }
                    is DataStatus.Success -> {
                        Log.d(it.data.toString())
                        it.data?.let { version ->
                            viewModel.getAllChampion(version)
                            viewModel.getAllGameItem(version)
                        } ?: Log.e("version is null!")
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_content) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bnvLolCategory, navController)
    }

    override fun permissionGranted(requestCode: Int) {
        showToast("권한 승인", Toast.LENGTH_SHORT)
    }

    override fun permissionDenied(requestCode: Int) {
        showToast("권한 거부", Toast.LENGTH_SHORT)
    }
}