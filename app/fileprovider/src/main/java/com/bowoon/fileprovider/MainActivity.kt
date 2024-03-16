package com.bowoon.fileprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bowoon.commonutils.ContextUtils.showToast
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.fromApi
import com.bowoon.fileprovider.databinding.ActivityMainBinding
import com.bowoon.mediastore.ChooseItemList
import com.bowoon.mediastore.MediaDataClass
import com.bowoon.mediastore.MediaManager
import com.bowoon.permissionmanager.requestMultiplePermission
import com.bowoon.permissionmanager.requestPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "fileprovider_main_activity"
    }

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }

    @Inject
    lateinit var mediaManager: MediaManager
    private val getContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Log.d(TAG, it.toString())
                when (Uri.EMPTY.equals(it)) {
                    true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
                    false -> {
                        mediaManager.getWrapFile(uri)?.let {
                            startActivity(
                                Intent(this@MainActivity, ContentsActivity::class.java).apply {
                                    putExtra(
                                        ContentsActivity.CONTENTS,
                                        ChooseItemList(listOf(it))
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    private val getMultipleContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            uriList.map { uri ->
                mediaManager.getWrapFile(uri)
            }.run {
                this.filterNotNull().run {
                    forEach { Log.d(TAG, it.toString()) }
                    when (this.isEmpty()) {
                        true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
                        false -> startActivity(this)
                    }
                }
            }
        }
    private val getDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                Log.d(TAG, it.toString())
                when (Uri.EMPTY.equals(it)) {
                    true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
                    false -> {
                        mediaManager.getWrapFile(uri)?.let {
                            startActivity(
                                Intent(this@MainActivity, ContentsActivity::class.java).apply {
                                    putExtra(
                                        ContentsActivity.CONTENTS,
                                        ChooseItemList(listOf(it))
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    private val getMultipleDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uriList ->
            uriList.map { uri ->
                Log.d(TAG, uri.toString())
                mediaManager.getWrapFile(uri)
            }.run {
                forEach { Log.d(TAG, it.toString()) }
                this.filterNotNull().run {
                    when (this.isEmpty()) {
                        true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
                        false -> startActivity(this)
                    }
                }
            }
        }
    private val grantedLambda: () -> Unit = {
        Log.d(TAG, "granted")
//        mediaManager.findImage(
//            projection = arrayOf(
//                MediaStore.Images.Media._ID,
//                MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.SIZE
//            ),
//            sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC",
//            action = { cursor ->
//                val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
//                val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
//                val sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
//
//                while (cursor.moveToNext()) {
//                    val id = cursor.getLongOrNull(idColumn)
//                    val name = cursor.getStringOrNull(nameColumn)
//                    val size = cursor.getIntOrNull(sizeColumn)?.run {
//                        mediaManager.getCapacity(this.toFloat(), listOf("KB", "MB", "GB", "TB")).run {
//                            "$first$second"
//                        }
//                    }
//                    val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id ?: continue)
//                    val mime = mediaManager.getMimeType(contentUri)
//                    val extension = mediaManager.getFileExtension(contentUri)
//
//                    Log.d(TAG, Image(contentUri.toString(), name, size, mime, extension).toString())
//                }
//            }
//        )
//        mediaManager.findVideo(
//            projection = arrayOf(
//                MediaStore.Video.Media._ID,
//                MediaStore.Video.Media.DISPLAY_NAME,
//                MediaStore.Video.Media.DURATION,
//                MediaStore.Video.Media.SIZE
//            ),
//            selection = "${MediaStore.Video.Media.DURATION} >= ?",
//            selectionArgs = arrayOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES).toString()),
//            sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC",
//            action = { cursor ->
//                val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
//                val nameColumn = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
//                val durationColumn = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
//                val sizeColumn = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
//
//                while (cursor.moveToNext()) {
//                    val id = cursor.getLongOrNull(idColumn)
//                    val name = cursor.getStringOrNull(nameColumn)
//                    val duration = cursor.getLongOrNull(durationColumn)
//                    val size = cursor.getIntOrNull(sizeColumn)?.run {
//                        mediaManager.getCapacity(this.toFloat(), listOf("KB", "MB", "GB", "TB")).run {
//                            "$first$second"
//                        }
//                    }
//                    val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id ?: continue)
//                    val mime = mediaManager.getMimeType(contentUri)
//                    val extension = mediaManager.getFileExtension(contentUri)
//
//                    Log.d(TAG, Video(contentUri.toString(), name, duration, size, mime, extension).toString())
//                }
//            }
//        )
//        mediaManager.findAudio(
//            projection = arrayOf(
//                MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.DISPLAY_NAME,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media.SIZE
//            ),
//            sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC",
//            action = { cursor ->
//                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
//                val nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
//                val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
//                val sizeColumn = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
//
//                while (cursor.moveToNext()) {
//                    val id = cursor.getLongOrNull(idColumn)
//                    val name = cursor.getStringOrNull(nameColumn)
//                    val duration = cursor.getLongOrNull(durationColumn)
//                    val size = cursor.getIntOrNull(sizeColumn)?.run {
//                        mediaManager.getCapacity(this.toFloat(), listOf("KB", "MB", "GB", "TB")).run {
//                            "$first$second"
//                        }
//                    }
//                    val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id ?: continue)
//                    val mime = mediaManager.getMimeType(contentUri)
//                    val extension = mediaManager.getFileExtension(contentUri)
//
//                    Log.d(TAG, Audio(contentUri.toString(), name, duration, size, mime, extension).toString())
//                }
//            }
//        )
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

        if (fromApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE, true)) {
            requestMultiplePermission(grantedLambda, deniedLambda)
                .launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                )
        } else if (fromApi(Build.VERSION_CODES.TIRAMISU, true)) {
            requestMultiplePermission(grantedLambda, deniedLambda)
                .launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                )
        } else {
            requestPermission(grantedLambda, deniedLambda)
                .launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun initBinding() {
        binding.apply {
            handler = ClickHandler()
        }
    }

    private fun startActivity(mediaList: List<MediaDataClass>) {
        when (mediaList.isEmpty()) {
            true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
            false -> {
                startActivity(
                    Intent(this@MainActivity, ContentsActivity::class.java).apply {
                        putExtra(
                            ContentsActivity.CONTENTS,
                            ChooseItemList(mediaList)
                        )
                    }
                )
            }
        }
    }

    inner class ClickHandler {
        fun getImageContent() {
//            getContentLauncher.launch(MediaManager.MediaType.IMAGE.mimeType)
            getMultipleContentLauncher.launch(MediaManager.MediaType.IMAGE.mimeType)
        }

        fun getVideoContent() {
//            getContentLauncher.launch(MediaManager.MediaType.VIDEO.mimeType)
            getMultipleContentLauncher.launch(MediaManager.MediaType.VIDEO.mimeType)
        }

        fun getAudioContent() {
//            getContentLauncher.launch(MediaManager.MediaType.AUDIO.mimeType)
            getMultipleContentLauncher.launch(MediaManager.MediaType.AUDIO.mimeType)
        }

        fun getDocumentContent() {
            getMultipleDocumentLauncher.launch(
                arrayOf(
                    MediaManager.MediaType.IMAGE.mimeType,
                    MediaManager.MediaType.VIDEO.mimeType,
                    MediaManager.MediaType.AUDIO.mimeType,
//                    MediaManager.MediaType.ALL.mimeType
                )
            )
        }

        fun deleteInternalFiles() {
            cacheDir.listFiles()
                ?.filter { it.name.startsWith("copyFile_") }
                ?.forEach {
                    Log.d(TAG, it.name)
                    it.delete()
                }
            cacheDir.listFiles()
                ?.filter { it.name.startsWith("copyFile_") }
                ?.forEach {
                    Log.d(TAG, it.name)
                    it.delete()
                }
            filesDir.listFiles()
                ?.filter { it.name.startsWith("copyFile_") }
                ?.forEach {
                    Log.d(TAG, it.name)
                    it.delete()
                }
            externalCacheDir?.listFiles()
                ?.filter { it.name.startsWith("copyFile_") }
                ?.forEach {
                    Log.d(TAG, it.name)
                    it.delete()
                }
            externalMediaDirs?.forEach { mediaFile ->
                mediaFile?.listFiles()
                    ?.filter { it.name.startsWith("copyFile_") }
                    ?.forEach { file ->
                        Log.d(TAG, file.name)
                        file.delete()
                    }
            }
        }
    }
}