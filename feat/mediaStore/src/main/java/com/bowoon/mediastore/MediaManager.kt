package com.bowoon.mediastore

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.fromApi
import com.bowoon.commonutils.toApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MediaManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "#MediaStore"
    }

    enum class StorageType {
        INTERNAL, EXTERNAL
    }

    enum class MediaType(val mimeType: String) {
        ALL("*/*"),
        IMAGE("image/*"),
        VIDEO("video/*"),
        AUDIO("audio/*")
    }

    private val capacitySuffix = listOf("KB", "MB", "GB", "TB")

    @SuppressLint("InlinedApi")
    private fun getMediaStoreUri(type: MediaType): Uri =
        when (fromApi(Build.VERSION_CODES.Q, true)) {
            true -> {
                when (type) {
                    MediaType.ALL -> MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    MediaType.AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            false -> {
                when (type) {
                    MediaType.ALL -> MediaStore.Files.getContentUri("external")
                    MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    MediaType.AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
        }

    fun getInternalCacheFiles(
        action: ((files: List<File>) -> Unit)?
    ) {
        action?.invoke(context.cacheDir.listFiles()?.toList() ?: emptyList())
    }

    fun getExternalCacheFiles(
        action: ((files: List<File>) -> Unit)?
    ) {
        if (isExternalStorageReadable()) {
            action?.invoke(context.externalCacheDir?.listFiles()?.toList() ?: emptyList())
        } else {
            action?.invoke(emptyList())
        }
    }

    fun getInternalFiles(
        action: ((files: List<File>) -> Unit)?
    ) {
        action?.invoke(context.filesDir.listFiles()?.toList() ?: emptyList())
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

    fun update(
        uri: Uri,
        contentValues: ContentValues? = null,
        where: String? = null,
        selectionArgs: Array<String>? = null,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ): Int = context.contentResolver.update(uri, contentValues, where, selectionArgs).run {
        if (this > 0) {
            onSuccess?.invoke()
        } else {
            onFailure?.invoke()
        }
        this
    }

    fun delete(
        uri: Uri,
        where: String? = null,
        selectionArgs: Array<String>? = null,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ): Int = context.contentResolver.delete(uri, where, selectionArgs).run {
        if (this > 0) {
            onSuccess?.invoke()
        } else {
            onFailure?.invoke()
        }
        this
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

    fun getFileContent(
        uri: Uri
    ): StringBuffer =
        context.contentResolver.openInputStream(uri).use { inputStream ->
            InputStreamReader(inputStream).use { isr ->
                StringBuffer().also { buffer ->
                    isr.readLines().also { strList ->
                        when (strList.isEmpty()) {
                            true -> buffer.append("$uri is empty")
                            false -> strList.forEach { buffer.append(it) }.run { buffer }
                        }
                    }
                }
            }
        }

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

    fun getUri(file: File): Uri? =
        FileProvider.getUriForFile(context, context.packageName, file)

    fun getExternalPublicStorageDirectory(type: String): File? =
        Environment.getExternalStoragePublicDirectory(type)

    tailrec fun getCapacity(size: Float, suffix: List<String>): Pair<Float, String> =
        when (size > 1024f) {
            true -> {
                if (suffix.size > 1) {
                    suffix.drop(1)
                }
                getCapacity(size / 1024, suffix)
            }
            false -> {
                Pair(size, suffix.first())
            }
        }

    fun getDuration(path: String): Long =
        runCatching {
            MediaMetadataRetriever().run {
                setDataSource(path)
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()?.let {
                    TimeUnit.MILLISECONDS.toSeconds(it)
                } ?: -1
            }
        }.getOrElse {
            Log.printStackTrace(it)
            -1
        }

    fun getMimeType(uri: Uri): String? =
        when {
            uri.scheme.equals(ContentResolver.SCHEME_CONTENT, true) -> {
                context.contentResolver.getType(uri)
            }
            uri.scheme.equals(ContentResolver.SCHEME_FILE, true) -> {
                MimeTypeMap.getFileExtensionFromUrl(uri.toString())?.run {
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(this)
                }
            }
            else -> null
        }

    fun getFileExtension(uri: Uri): String? =
        when {
            uri.scheme.equals(ContentResolver.SCHEME_CONTENT, true) -> MimeTypeMap.getSingleton().getExtensionFromMimeType(getMimeType(uri))
            uri.scheme.equals(ContentResolver.SCHEME_FILE, true) -> {
                uri.path?.let { path ->
                    MimeTypeMap.getFileExtensionFromUrl(path)
                } ?: run {
                    Log.e(TAG, "file extension not found...")
                    null
                }
            }
            else -> {
                Log.e(TAG, "uri scheme not supported...")
                null
            }
        }

    fun getWrapFile(fileUri: Uri): MediaDataClass? =
        when {
            fileUri.scheme.equals(ContentResolver.SCHEME_FILE, true) -> fileUri.toFile()
            fileUri.scheme.equals(ContentResolver.SCHEME_CONTENT, true) -> {
                context.contentResolver.openInputStream(fileUri)?.use {
                    getFileExtension(fileUri)?.run {
                        File.createTempFile("copyFile_", ".$this", context.cacheDir).apply {
                            it.copyTo(this.outputStream())
                            deleteOnExit()
                        }
                    } ?: run {
                        Log.e(TAG, "file extension unknown...")
                        null
                    }
                }
            }
            else -> null
        }?.let { file ->
            getMimeType(file.toUri())?.let { mime ->
                val size = getCapacity(file.length().toFloat(), capacitySuffix).run { "$first$second" }
                when {
                    mime.matches("(?i)image/(?i)[a-zA-Z0-9]*".toRegex()) -> Image(file.toUri().toString(), file.name, size, mime, getFileExtension(file.toUri()))
                    mime.matches("(?i)video/(?i)[a-zA-Z0-9]*".toRegex()) -> Video(file.toUri().toString(), file.name, getDuration(file.path), size, mime, getFileExtension(file.toUri()))
                    mime.matches("(?i)audio/(?i)[a-zA-Z0-9]*".toRegex()) -> Audio(file.toUri().toString(), file.name, getDuration(file.path), size, mime, getFileExtension(file.toUri()))
                    mime.matches("(?i)text/(?i)[a-zA-Z0-9]*".toRegex()) -> File(file.toUri().toString(), file.name, size, mime, getFileExtension(file.toUri()))
                    mime.matches("(?i)[a-zA-Z0-9]*/(?i)[a-zA-Z0-9]*".toRegex()) -> File(file.toUri().toString(), file.name, size, mime, getFileExtension(file.toUri()))
                    else -> {
                        Log.e(TAG, "mime not found...")
                        null
                    }
                }
            }
        } ?: run {
            Log.e(TAG, "file not found...")
            null
        }

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

    fun checkReadExternalStoragePermission(): Boolean =
        when (toApi(Build.VERSION_CODES.TIRAMISU, false)) {
            true -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            false -> false
        }

    // 쓰기 가능한 상태인지 체크
    fun isExternalStorageWritable(): Boolean =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    // 읽기 가능한 상태인지 체크
    fun isExternalStorageReadable(): Boolean =
        Environment.getExternalStorageState() in setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
}