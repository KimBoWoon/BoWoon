package com.bowoon.fileprovider

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.ContextUtils.sixteenByNineHeight
import com.bowoon.commonutils.Log
import com.bowoon.fileprovider.databinding.VhContentFileBinding
import com.bowoon.fileprovider.databinding.VhContentImageBinding
import com.bowoon.fileprovider.databinding.VhContentVideoBinding
import com.bowoon.mediastore.Audio
import com.bowoon.mediastore.File
import com.bowoon.mediastore.Image
import com.bowoon.mediastore.MediaDataClass
import com.bowoon.mediastore.Video
import java.io.FileInputStream
import java.io.InputStreamReader

class ContentsAdapter : ListAdapter<MediaDataClass, RecyclerView.ViewHolder>(diff) {
    companion object {
        private const val TAG = "fileprovider_contents_adapter"
        private val diff = object : DiffUtil.ItemCallback<MediaDataClass>() {
            override fun areItemsTheSame(oldItem: MediaDataClass, newItem: MediaDataClass): Boolean =
                when {
                    oldItem is Image && newItem is Image -> oldItem == newItem
                    oldItem is Video && newItem is Video -> oldItem == newItem
                    oldItem is Audio && newItem is Audio -> oldItem == newItem
                    oldItem is File && newItem is File -> oldItem == newItem
                    else -> false
                }

            override fun areContentsTheSame(oldItem: MediaDataClass, newItem: MediaDataClass): Boolean =
                when {
                    oldItem is Image && newItem is Image -> oldItem.uri == newItem.uri && oldItem.name == newItem.name
                    oldItem is Video && newItem is Video -> oldItem.uri == newItem.uri && oldItem.name == newItem.name
                    oldItem is Audio && newItem is Audio -> oldItem.uri == newItem.uri && oldItem.name == newItem.name
                    oldItem is File && newItem is File -> oldItem.uri == newItem.uri && oldItem.name == newItem.name
                    else -> false
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.vh_content_image -> ImageContentVH(VhContentImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.vh_content_video -> VideoContentVH(VhContentVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.vh_content_audio -> AudioContentVH(VhContentVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.vh_content_file -> FileContentVH(VhContentFileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw RuntimeException("view holder not found...")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currentList[position]?.let {
            when (holder) {
                is ImageContentVH -> holder.bind(it as? Image)
                is VideoContentVH -> holder.bind(it as? Video)
                is AudioContentVH -> holder.bind(it as? Audio)
                is FileContentVH -> holder.bind(it as? File)
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        currentList[position]?.let { mediaItem ->
            when (mediaItem) {
                is Audio -> R.layout.vh_content_audio
                is File -> R.layout.vh_content_file
                is Image -> R.layout.vh_content_image
                is Video -> R.layout.vh_content_video
                else -> -1
            }
        } ?: -1

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        when (holder) {
            is VideoContentVH -> holder.attach()
            is AudioContentVH -> holder.attach()
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        when (holder) {
            is VideoContentVH -> holder.release()
            is AudioContentVH -> holder.release()
        }
    }

    inner class ImageContentVH(
        private val binding: VhContentImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content: Image?) {
            content?.let {
                binding.apply {
                    ivContent.setImageURI(content.uri?.toUri())
                }
            }
        }
    }

    inner class VideoContentVH(
        private val binding: VhContentVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var content: Uri
        private val listener = ExoPlayerListener()

        fun bind(content: Video?) {
            content?.let {
                this@VideoContentVH.content = Uri.parse(content.uri)

                binding.apply {
                    exoplayer.apply {
                        layoutParams.height = binding.root.context.sixteenByNineHeight()
                        createPlayer()
                    }
                }
            }
        }

        private fun createPlayer() {
            binding.exoplayer.player = ExoPlayer.Builder(binding.root.context).build().apply {
                repeatMode = Player.REPEAT_MODE_ALL

//                val session = MediaSession.Builder(binding.root.context, this).build()
            }
        }

        private fun setMediaData() {
            MediaItem.Builder().setUri(content).build().apply {
                binding.exoplayer.player?.setMediaItem(this)
                binding.exoplayer.player?.prepare()
                binding.exoplayer.player?.play()
            }
            binding.exoplayer.player?.addListener(listener)
        }

        fun attach() {
            Log.d(TAG, "VideoContentVH attach")
            if (binding.exoplayer.player == null) {
                Log.d(TAG, "VideoContentVH player is null")
                createPlayer()
            }
            setMediaData()
        }

        fun release() {
            Log.d(TAG, "VideoContentVH release")
            binding.exoplayer.player?.removeListener(listener)
            binding.exoplayer.player?.release()
            binding.exoplayer.player = null
        }
    }

    inner class AudioContentVH(
        private val binding: VhContentVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var content: Uri
        private val listener = ExoPlayerListener()

        fun bind(content: Audio?) {
            content?.let {
                this@AudioContentVH.content = Uri.parse(content.uri)

                binding.apply {
                    exoplayer.apply {
                        layoutParams.height = binding.root.context.sixteenByNineHeight()
                        createPlayer()
                    }
                }
            }
        }

        private fun createPlayer() {
            binding.exoplayer.player = ExoPlayer.Builder(binding.root.context).build().apply {
                repeatMode = Player.REPEAT_MODE_ALL

//                val session = MediaSession.Builder(binding.root.context, this).build()
            }
        }

        private fun setMediaData() {
            MediaItem.Builder().setUri(content).build().apply {
                binding.exoplayer.player?.setMediaItem(this)
                binding.exoplayer.player?.prepare()
                binding.exoplayer.player?.play()
            }
            binding.exoplayer.player?.addListener(listener)
        }

        fun attach() {
            Log.d(TAG, "VideoContentVH attach")
            if (binding.exoplayer.player == null) {
                Log.d(TAG, "VideoContentVH player is null")
                createPlayer()
            }
            setMediaData()
        }

        fun release() {
            Log.d(TAG, "VideoContentVH release")
            binding.exoplayer.player?.removeListener(listener)
            binding.exoplayer.player?.release()
            binding.exoplayer.player = null
        }
    }

    inner class FileContentVH(
        private val binding: VhContentFileBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content: File?) {
            content?.let {
                binding.apply {
                    content.takeIf {
                        it.mime?.matches("(?i)text/(?i)plain".toRegex()) ?: false
                    }?.run {
                        uri?.let {
                            FileInputStream(it).use { fis ->
                                InputStreamReader(fis).use { isr ->
                                    val data = isr.readLines()
                                    Log.d(TAG, data.toString())
                                    when (data.isEmpty()) {
                                        true -> tvContent.text = String.format("%s is empty", this.name)
                                        false -> {
                                            data.forEach { str ->
                                                tvContent.text = String.format("%s %s", tvContent.text.toString(), str)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class ExoPlayerListener : Player.Listener {
    companion object {
        private const val TAG = "fileprovider_exoplayer_listener"
    }
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        if (playWhenReady) {
            Log.d(TAG, "onPlayWhenReadyChanged true")
        } else {
            Log.d(TAG, "onPlayWhenReadyChanged false")
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_READY -> Log.d(TAG, "STATE_READY")
            Player.STATE_BUFFERING -> Log.d(TAG, "STATE_BUFFERING")
            Player.STATE_ENDED -> Log.d(TAG, "STATE_ENDED")
            Player.STATE_IDLE -> Log.d(TAG, "STATE_IDLE")
        }
    }
}