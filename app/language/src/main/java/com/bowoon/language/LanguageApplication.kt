package com.bowoon.language

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bowoon.datamanager.DataStoreRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class LanguageApplication : Application() {
    @Inject
    lateinit var datastore: DataStoreRepository

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Main).launch {
            val language = getLanguage()
            if (language.isNotEmpty()) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        CoroutineScope(Dispatchers.Main).launch {
            val language = getLanguage()
            if (language.isNotEmpty()) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
            }
        }
    }

    suspend fun getLanguage(): String =
        datastore.getData("test_app", stringPreferencesKey("language"), "") ?: ""
}