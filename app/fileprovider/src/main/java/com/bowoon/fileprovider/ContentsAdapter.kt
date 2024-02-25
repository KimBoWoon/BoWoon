package com.bowoon.fileprovider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
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
        fun bind(content: Video?) {
            content?.let {
                binding.apply {
                    exoplayer.apply {
                        layoutParams.height = binding.root.context.sixteenByNineHeight()
                        player = ExoPlayer.Builder(binding.root.context).build().also {
//                        val session = MediaSession.Builder(binding.root.context, this).build()
                            MediaItem.fromUri(content.uri ?: "").apply {
                                it.setMediaItem(this)
                                it.prepare()
                            }
                        }
                    }
                }
            }
        }
    }

    inner class AudioContentVH(
        private val binding: VhContentVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content: Audio?) {
            content?.let {
                binding.apply {
                    exoplayer.apply {
                        layoutParams.height = binding.root.context.sixteenByNineHeight()
                        player = ExoPlayer.Builder(binding.root.context).build().also {
//                        val session = MediaSession.Builder(binding.root.context, this).build()
                            MediaItem.fromUri(content.uri ?: "").apply {
                                it.setMediaItem(this)
                                it.prepare()
                            }
                        }
                    }
                }
            }
        }
    }

    inner class FileContentVH(
        private val binding: VhContentFileBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content: File?) {
            Log.d(TAG, content.toString())
            content?.let {
                binding.apply {
                    tvContent.text = content.name
                }
            }
        }
    }
}