package com.lol.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.lol.R
import com.lol.base.BaseActivity
import com.lol.databinding.ActivityMainBinding
import com.lol.ui.vm.MainVM
import dagger.hilt.android.AndroidEntryPoint

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
    }

    override fun initBinding() {}

    override fun initFlow() {}

//    private fun initNavigation() {
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_content) as NavHostFragment
//        val navController = navHostFragment.navController
//        NavigationUI.setupWithNavController(binding.bottom, navController)
//    }
}