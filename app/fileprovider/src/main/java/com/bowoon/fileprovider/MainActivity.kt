package com.bowoon.fileprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.databinding.DataBindingUtil
import com.bowoon.commonutils.ContextUtils.showToast
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.ViewAdapter.onDebounceClickListener
import com.bowoon.commonutils.fromApi
import com.bowoon.fileprovider.databinding.ActivityMainBinding
import com.bowoon.mediastore.Audio
import com.bowoon.mediastore.ChooseItemList
import com.bowoon.mediastore.File
import com.bowoon.mediastore.FileInfo
import com.bowoon.mediastore.Image
import com.bowoon.mediastore.MediaDataClass
import com.bowoon.mediastore.MediaManager
import com.bowoon.mediastore.Video
import com.bowoon.permissionmanager.requestMultiplePermission
import com.bowoon.permissionmanager.requestPermission
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
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
                getFileInfo(it).also { (name, mime) ->
                    when (Uri.EMPTY.equals(it)) {
                        true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
                        false -> {
                            if (!name.isNullOrEmpty() && !mime.isNullOrEmpty()) {
                                startActivity(Intent(this@MainActivity, ContentsActivity::class.java).apply {
                                    putExtra(
                                        ContentsActivity.CONTENTS,
                                        ChooseItemList(list = listOf(getFile(uri, name, mime)))
                                    )
                                })
                            }
                        }
                    }
                }
            }
        }
    private val getMultipleContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            uriList.map { uri ->
                getFileInfo(uri).let { (name, mime) ->
                    if (!name.isNullOrEmpty() && !mime.isNullOrEmpty()) {
                        getFile(uri, name, mime)
                    } else {
                        File(null, null, null)
                    }
                }
            }.run {
                when (this.isEmpty()) {
                    true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
                    false -> startActivity(this@run)
                }
            }
        }
    private val getMultipleDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uriList ->
            uriList.map { uri ->
                getFileInfo(uri).let { (name, mime) ->
                    if (!name.isNullOrEmpty() && !mime.isNullOrEmpty()) {
                        getFile(uri, name, mime)
                    } else {
                        File(null, null, null)
                    }
                }
            }.run {
                when (this.isEmpty()) {
                    true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
                    false -> startActivity(this@run)
                }
            }
        }
    private val grantedLambda: () -> Unit = {
        Log.d(TAG, "granted")
        mediaManager.findImage(
            projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
            ),
            sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC",
            action = { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLongOrNull(idColumn)
                    val name = cursor.getStringOrNull(nameColumn)
                    val size = cursor.getIntOrNull(sizeColumn)
                    val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id ?: continue).toString()

                    Log.d(TAG, Image(contentUri, name, size).toString())
                }
            }
        )
        mediaManager.findVideo(
            projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
            ),
            selection = "${MediaStore.Video.Media.DURATION} >= ?",
            selectionArgs = arrayOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES).toString()),
            sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC",
            action = { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
                val nameColumn = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLongOrNull(idColumn)
                    val name = cursor.getStringOrNull(nameColumn)
                    val duration = cursor.getIntOrNull(durationColumn)
                    val size = cursor.getIntOrNull(sizeColumn)
                    val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id ?: continue).toString()

                    Log.d(TAG, Video(contentUri, name, duration, size).toString())
                }
            }
        )
        mediaManager.findAudio(
            projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE
            ),
            sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC",
            action = { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                val sizeColumn = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLongOrNull(idColumn)
                    val name = cursor.getStringOrNull(nameColumn)
                    val duration = cursor.getIntOrNull(durationColumn)
                    val size = cursor.getIntOrNull(sizeColumn)
                    val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id ?: continue).toString()

                    Log.d(TAG, Audio(contentUri, name, duration, size).toString())
                }
            }
        )
        mediaManager.findText(
            projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MIME_TYPE
            ),
            selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?",
            selectionArgs = arrayOf(MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt") ?: "txt"),
//            selectionArgs = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_NONE.toString()),
            sortOrder = "${MediaStore.Files.FileColumns.DISPLAY_NAME} ASC",
            action = { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val nameColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
                val mimeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLongOrNull(idColumn)
                    val name = cursor.getStringOrNull(nameColumn)
                    val size = cursor.getIntOrNull(sizeColumn)
                    val mime = cursor.getStringOrNull(mimeColumn)
                    val contentUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id ?: continue).toString()

                    Log.d(TAG, File(contentUri, name, size).toString())
                }
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
            bImage.onDebounceClickListener {
//                getContentLauncher.launch(MediaStore.MediaType.IMAGE.mimeType)
                getMultipleContentLauncher.launch(MediaManager.MediaType.IMAGE.mimeType)
//                getMultipleDocumentLauncher.launch(arrayOf(MediaManager.MediaType.IMAGE.mimeType))
            }
            bVideo.onDebounceClickListener {
//                getContentLauncher.launch(MediaStore.MediaType.VIDEO.mimeType)
                getMultipleContentLauncher.launch(MediaManager.MediaType.VIDEO.mimeType)
//                getMultipleDocumentLauncher.launch(arrayOf(MediaManager.MediaType.VIDEO.mimeType))
            }
            bAudio.onDebounceClickListener {
//                getContentLauncher.launch(MediaStore.MediaType.AUDIO.mimeType)
                getMultipleContentLauncher.launch(MediaManager.MediaType.AUDIO.mimeType)
//                getMultipleDocumentLauncher.launch(arrayOf(MediaManager.MediaType.AUDIO.mimeType))
            }
            bText.onDebounceClickListener {
//                getContentLauncher.launch(MediaStore.MediaType.FILE.mimeType)
                getMultipleContentLauncher.launch(MediaManager.MediaType.TEXT.mimeType)
//                getMultipleDocumentLauncher.launch(arrayOf(MediaManager.MediaType.TEXT.mimeType))
            }
            bDocument.onDebounceClickListener {
                getMultipleDocumentLauncher.launch(arrayOf(MediaManager.MediaType.ALL.mimeType))
            }
        }
    }

    private fun getFileInfo(uri: Uri): FileInfo = mediaManager.getFileInfo(
        uri,
        arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
        null,
        null,
        null
    ) { cursor ->
        val nameColumn = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val name = cursor.getStringOrNull(nameColumn)
        Log.d(TAG, "name > $name, mime > ${mediaManager.getMimeType(uri)}")
        FileInfo(name, mediaManager.getMimeType(uri))
    }

    private fun getFile(uri: Uri, name: String, mime: String): MediaDataClass =
        when {
            mime.startsWith("image", true) -> Image(uri.toString(), name)
            mime.startsWith("video", true) -> Video(uri.toString(), name)
            mime.startsWith("audio", true) -> Audio(uri.toString(), name)
            mime.startsWith("text", true) -> File(uri.toString(), name)
            mime.startsWith("*/*", true) -> File(uri.toString(), name)
            else -> {
                Log.e(TAG, "mime not found...")
                File(null, null, null)
            }
        }

    private fun startActivity(mediaList: List<MediaDataClass>) {
        when (mediaList.isEmpty()) {
            true -> showToast("선택한 파일이 없습니다.", Toast.LENGTH_SHORT)
            false -> {
                startActivity(Intent(this@MainActivity, ContentsActivity::class.java).apply {
                    putExtra(
                        ContentsActivity.CONTENTS,
                        ChooseItemList(list = mediaList)
                    )
                })
            }
        }
    }
}