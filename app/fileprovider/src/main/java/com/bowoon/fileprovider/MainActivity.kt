package com.bowoon.fileprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.databinding.DataBindingUtil
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.fromApi
import com.bowoon.fileprovider.databinding.ActivityMainBinding
import com.bowoon.mediastore.Audio
import com.bowoon.mediastore.Image
import com.bowoon.mediastore.MediaStore
import com.bowoon.mediastore.Video
import com.bowoon.permissionmanager.requestMultiplePermission
import com.bowoon.permissionmanager.requestPermission
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "#MainActivity"
    }

    enum class MediaType(val label: String) {
        IMAGE("image/*"),
        VIDEO("video/*"),
        AUDIO("audio/*")
    }

    @Inject
    lateinit var mediaStore: MediaStore
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }
    private val getContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            mediaStore.getPath(it)?.let { path ->
                File(cacheDir, "test.jpg").apply {
                    if (this.exists()) {
                        this.delete()
                    }
                    if (createNewFile()) {
                        File(path).copyTo(this, true)
//                        Log.d(TAG, path)
//                        Log.d(TAG, uri.path.toString())
//                        Log.d(TAG, getExternalFilesDir(Environment.DIRECTORY_DCIM)?.path ?: "")
//                        Log.d(TAG, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)?.path ?: "")
//                        Log.d(TAG, File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "android_logo.png").path ?: "")
//                        Log.d(TAG, mediaStore.getUri(this@MainActivity, File(mediaStore.getExternalPublicStorageDirectory(Environment.DIRECTORY_DCIM), "android_logo.png").path).toString())
                        mediaStore.saveFile(
                            File(path).copyTo(this, true),
                            "mediaTypeText.jpg",
                            MediaStore.MediaType.IMAGE,
                            { Log.d(TAG, "file save success") },
                            { Log.d(TAG, "file save failure") }
                        )
                    }
                }
            }
//            startActivity(Intent(this@MainActivity, ContentsActivity::class.java).apply {
//                putExtra(ContentsActivity.CONTENTS, ChooseItemList(list = listOf(Image(it.toString()))))
//            })
        }
    }
    private val getMultipleContentLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
        uriList.map {
            Image(it.toString())
        }.run {
            startActivity(Intent(this@MainActivity, ContentsActivity::class.java).apply {
                putExtra(ContentsActivity.CONTENTS, ChooseItemList(this@run))
            })
        }
    }
    private val grantedLambda: () -> Unit = {
        Log.d(TAG, "granted")
        mediaStore.findImage(
            projection = arrayOf(
                android.provider.MediaStore.Images.Media._ID,
                android.provider.MediaStore.Images.Media.DISPLAY_NAME,
                android.provider.MediaStore.Images.Media.SIZE
            ),
            sortOrder = "${android.provider.MediaStore.Images.Media.DISPLAY_NAME} ASC",
            action = { cursor ->
                val result = mutableListOf<Image>()
                val idColumn = cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE)

                while (cursor.moveToNext()) {
                    when (idColumn != -1 && nameColumn != -1 && sizeColumn != -1) {
                        true -> {
                            val id = cursor.getLongOrNull(idColumn)
                            val name = cursor.getStringOrNull(nameColumn)
                            val size = cursor.getIntOrNull(sizeColumn)
                            val contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id ?: continue).toString()

                            result.add(Image(contentUri, name, size))
                        }
                        false -> Log.e(TAG, "column not found...")
                    }
                }

//                result.forEach {
//                    Log.d(TAG, it.toString())
//                }
            }
        )
        mediaStore.findVideo(
            projection = arrayOf(
                android.provider.MediaStore.Video.Media._ID,
                android.provider.MediaStore.Video.Media.DISPLAY_NAME,
                android.provider.MediaStore.Video.Media.DURATION,
                android.provider.MediaStore.Video.Media.SIZE
            ),
            selection = "${android.provider.MediaStore.Video.Media.DURATION} >= ?",
            selectionArgs = arrayOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES).toString()),
            sortOrder = "${android.provider.MediaStore.Video.Media.DISPLAY_NAME} ASC",
            action = { cursor ->
                val result = mutableListOf<Video>()
                val idColumn = cursor.getColumnIndex(android.provider.MediaStore.Video.Media._ID)
                val nameColumn = cursor.getColumnIndex(android.provider.MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndex(android.provider.MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndex(android.provider.MediaStore.Video.Media.SIZE)

                while (cursor.moveToNext()) {
                    when (idColumn != -1 && nameColumn != -1 && durationColumn != -1 && sizeColumn != -1) {
                        true -> {
                            val id = cursor.getLongOrNull(idColumn)
                            val name = cursor.getStringOrNull(nameColumn)
                            val duration = cursor.getIntOrNull(durationColumn)
                            val size = cursor.getIntOrNull(sizeColumn)
                            val contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id ?: continue).toString()

                            result.add(Video(contentUri, name, size))
                        }
                        false -> Log.e(TAG, "column not found...")
                    }
                }

//                result.forEach {
//                    Log.d(TAG, it.toString())
//                }
            }
        )
        mediaStore.findAudio(
            projection = arrayOf(
                android.provider.MediaStore.Audio.Media._ID,
                android.provider.MediaStore.Audio.Media.DISPLAY_NAME,
                android.provider.MediaStore.Audio.Media.DURATION,
                android.provider.MediaStore.Audio.Media.SIZE
            ),
            sortOrder = "${android.provider.MediaStore.Audio.Media.DISPLAY_NAME} ASC",
            action = { cursor ->
                val result = mutableListOf<Audio>()
                val idColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Audio.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Audio.Media.SIZE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLongOrNull(idColumn)
                    val name = cursor.getStringOrNull(nameColumn)
                    val duration = cursor.getIntOrNull(durationColumn)
                    val size = cursor.getIntOrNull(sizeColumn)
                    val contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id ?: continue).toString()

                    result.add(Audio(contentUri, name, duration, size))
                }

//                result.forEach {
//                    Log.d(TAG, it.toString())
//                }
            }
        )
    }
    private val deniedLambda: () -> Unit = {
        Log.d(TAG, "denied")
    }

    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
        }

        initBinding()

        if (fromApi(Build.VERSION_CODES.TIRAMISU, true)) {
            requestMultiplePermission(grantedLambda, deniedLambda).launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO))
        } else {
            requestPermission(grantedLambda, deniedLambda).launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun initBinding() {
        binding.apply {
            binding.bImage.setOnClickListener {
                getContentLauncher.launch(MediaType.IMAGE.label)
//                getMultipleContentLauncher.launch(MediaType.IMAGE.label)
            }
        }
    }
}