package com.bowoon.mediastore

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.fromApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class MediaManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "#MediaStore"
    }

    enum class MediaType(val mimeType: String) {
        ALL("*/*"),
        IMAGE("image/*"),
        VIDEO("video/*"),
        AUDIO("audio/*"),
        TEXT("text/*")
    }

    @SuppressLint("InlinedApi")
    private fun getMediaStoreUri(type: MediaType): Uri =
        when (fromApi(Build.VERSION_CODES.Q, true)) {
            true -> {
                when (type) {
                    MediaType.ALL -> MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    MediaType.IMAGE -> MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    MediaType.VIDEO -> MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    MediaType.AUDIO -> MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    MediaType.TEXT -> MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
                }
            }
            false -> {
                when (type) {
                    MediaType.ALL -> MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    MediaType.AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    MediaType.TEXT -> MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
                }
            }
        }

    fun find(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null,
        action: ((cursor: Cursor) -> Unit)? = null
    ) {
        context.contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            action?.invoke(cursor)
        } ?: run {
            Log.e(TAG, "content not found...")
        }
    }

    fun getFileInfo(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null,
        action: ((cursor: Cursor) -> FileInfo)
    ): FileInfo = context.contentResolver.query(
        uri,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )?.use { cursor ->
        action.invoke(cursor)
    } ?: run {
        Log.e(TAG, "content not found...")
        FileInfo(null, null)
    }

    fun findImage(
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null,
        action: ((cursor: Cursor) -> Unit)? = null
    ) {
        find(
            getMediaStoreUri(MediaType.IMAGE),
            projection,
            selection,
            selectionArgs,
            sortOrder,
            action
        )
    }

    fun findVideo(
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null,
        action: ((cursor: Cursor) -> Unit)? = null
    ) {
        find(
            getMediaStoreUri(MediaType.VIDEO),
            projection,
            selection,
            selectionArgs,
            sortOrder,
            action
        )
    }

    fun findAudio(
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null,
        action: ((cursor: Cursor) -> Unit)? = null
    ) {
        find(
            getMediaStoreUri(MediaType.AUDIO),
            projection,
            selection,
            selectionArgs,
            sortOrder,
            action
        )
    }

    fun findText(
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null,
        action: ((cursor: Cursor) -> Unit)? = null
    ) {
        find(
            getMediaStoreUri(MediaType.TEXT),
            projection,
            selection,
            selectionArgs,
            sortOrder,
            action
        )
    }

    fun save(
        file: File,
        name: String,
        type: MediaType,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        ContentValues().apply {
            this@apply.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            this@apply.put(MediaStore.MediaColumns.MIME_TYPE, type.mimeType)
            if (fromApi(Build.VERSION_CODES.Q)) {
                this@apply.put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            context.contentResolver.insert(getMediaStoreUri(type), this@apply)?.let { uri ->
                context.contentResolver.openFileDescriptor(uri, "w", null)?.use { pdf ->
                    FileOutputStream(pdf.fileDescriptor).use { fos ->
                        fos.write(file.readBytes())
                    }
                    if (fromApi(Build.VERSION_CODES.Q)) {
                        this@apply.clear()
                        this@apply.put(MediaStore.MediaColumns.IS_PENDING, 0)
                        context.contentResolver.update(uri, this@apply, null, null)
                    }
                    onSuccess?.invoke()
                } ?: run {
                    Log.e(TAG, "file description is null...")
                    onFailure?.invoke()
                }
            } ?: run {
                Log.e(TAG, "uri is null...")
                onFailure?.invoke()
            }
        }
    }

    fun getMimeType(uri: Uri): String? = context.contentResolver.getType(uri)

    fun getPath(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.MediaColumns.DATA)
        context.contentResolver.query(
            contentUri,
            proj,
            null,
            null,
            null
        )?.use { cursor ->
            when (cursor.moveToNext()) {
                true -> {
                    cursor.getColumnIndex(MediaStore.MediaColumns.DATA).also { index ->
                        cursor.getStringOrNull(index)?.let { path ->
                            return path
                        }
                    }
                }
                false -> Log.e(TAG, "cursor has no item...")
            }
        }

        return null
    }

    fun getUri(filePath: String): Uri? {
        val proj = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DATA} = ?"
        val selectionArgs = arrayOf(filePath)
        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            proj,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            when (cursor.moveToNext()) {
                true -> {
                    cursor.getColumnIndex(MediaStore.MediaColumns._ID).also { index ->
                        cursor.getIntOrNull(index)?.let { id ->
                            return ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id.toLong()
                            )
                        }
                    }
                }
                false -> Log.e(TAG, "cursor has no item...")
            }
        }

        return null
    }

    fun getUri(file: File): String =
        FileProvider.getUriForFile(context, context.packageName, file).toString()

    fun getExternalPublicStorageDirectory(type: String): File? =
        Environment.getExternalStoragePublicDirectory(type)

    @SuppressLint("InlinedApi")
    fun checkAudioPermission(): Boolean =
        when (fromApi(Build.VERSION_CODES.TIRAMISU, true)) {
            true -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
            false -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

    @SuppressLint("InlinedApi")
    fun checkVideoPermission(): Boolean =
        when (fromApi(Build.VERSION_CODES.TIRAMISU, true)) {
            true -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
            false -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

    @SuppressLint("InlinedApi")
    fun checkImagePermission(): Boolean =
        when (fromApi(Build.VERSION_CODES.TIRAMISU, true)) {
            true -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
            false -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
}