package com.bowoon.language

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.bowoon.commonutils.isDarkMode
import com.bowoon.datamanager.DataStoreRepository
import com.bowoon.language.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }

    @Inject
    lateinit var datastore: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initBinding()
    }

    private fun initBinding() {
        binding.apply {
            tvResult.setTextColor(
                when (isDarkMode()) {
                    true -> Color.WHITE
                    false -> Color.BLACK
                }
            )
            bKorean.setOnClickListener {
                setLanguage("ko")
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ko"))
//                setLocale(this@MainActivity, "ko")
//                recreate()
            }
            bEnglish.setOnClickListener {
                setLanguage("en")
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
//                setLocale(this@MainActivity, "en")
//                recreate()
            }
            bJapanese.setOnClickListener {
                setLanguage("ja")
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ja"))
//                setLocale(this@MainActivity, "ja")
//                recreate()
            }
            tvResult.text = getString(R.string.main_text)
        }
    }

    private fun setLanguage(locale: String) {
        lifecycleScope.launch {
            datastore.setData("test_app", stringPreferencesKey("language"), locale)
        }
    }

    fun setLocale(con: Context, lan: String) {
        val countryCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            con.resources.configuration.locales.get(0).country
        } else {
            con.resources.configuration.locale.country
        }
        val locale = Locale(lan, countryCode)
        if (TextUtils.isEmpty(Locale.getDefault().country)) {
            // 디바이스의 기본 로케일 정보를 바꾸는 부분이라서 가져오는 정보가 없을 때만 디바이스의 로케일 정보 다시 설정.
            Locale.setDefault(locale)
        }
        val config = Configuration().apply {
            setLocale(locale)
        }
//        con.createConfigurationContext(config)
        con.resources.updateConfiguration(config, con.resources.displayMetrics)
    }
}