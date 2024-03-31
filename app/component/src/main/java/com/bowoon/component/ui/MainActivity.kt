package com.bowoon.component.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bowoon.commonutils.Log
import com.bowoon.component.adapters.ComponentAdapter
import com.bowoon.component.data.ComponentData
import com.bowoon.component.data.Components
import com.bowoon.component.R
import com.bowoon.component.databinding.ActivityMainBinding
import com.bowoon.component.utils.ComponentUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "component_main_activity"
    }

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }
    @Inject
    lateinit var json: Json
    @Inject
    lateinit var componentUtils: ComponentUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding.apply {
            lifecycleOwner = this@MainActivity
        }

        assets.open("component.json").use { inputStream ->
            runCatching {
                json.decodeFromString<ComponentData>(String(inputStream.readBytes(), Charsets.UTF_8))
            }.onSuccess {
                Log.d(TAG, it.toString())
                mutableListOf<Components>().apply {
                    it.components?.filterNotNull()?.forEach { component ->
                        add(componentUtils.createComponent(component))
                    }
                }.run {
                    binding.rvComponentList.adapter = ComponentAdapter().apply {
                        submitList(this@run)
                    }
                }
            }.onFailure { e ->
                Log.printStackTrace(e)
            }
        }
    }
}