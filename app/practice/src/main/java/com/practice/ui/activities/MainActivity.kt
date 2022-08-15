package com.practice.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.practice.R
import com.practice.base.BaseActivity
import com.practice.databinding.ActivityMainBinding
import com.practice.ui.activities.vm.MainVM
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
        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_content) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottom, navController)
    }

    override fun initBinding() {
//        TODO("Not yet implemented")
    }

    override fun initFlow() {
//        TODO("Not yet implemented")
    }
}