package com.bowoon.fileprovider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.ContextUtils.getScreenWidth
import com.bowoon.commonutils.getSafetyParcelableExtra
import com.bowoon.fileprovider.databinding.ActivityContentsBinding
import com.bowoon.fileprovider.databinding.VhContentImageBinding
import com.bowoon.fileprovider.databinding.VhContentVideoBinding
import com.bowoon.mediastore.Image
import com.bowoon.mediastore.Video
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentsActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "#ContentsActivity"
        const val CONTENTS = "CONTENTS"
    }

    private val binding: ActivityContentsBinding by lazy {
        DataBindingUtil.setContentView(this@ContentsActivity, R.layout.activity_contents)
    }
    private lateinit var imageList: List<Image>
    private lateinit var videoList: List<Video>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@ContentsActivity
        }

        intent.getSafetyParcelableExtra<ChooseItemList>(CONTENTS)?.run {
            this.imageList?.let { this@ContentsActivity.imageList = it }
            this.videoList?.let { this@ContentsActivity.videoList = it }
        }

        initBinding()
    }

    private fun initBinding() {
        binding.apply {
            rvContentsList.apply {
                layoutManager = GridLayoutManager(this@ContentsActivity, 1, RecyclerView.VERTICAL, false)
                adapter = VideoContentsAdapter().apply {
//                    submitList(imageList)
                    submitList(videoList)
                }
            }
        }
    }
}

class ImageContentsAdapter : ListAdapter<Image, ImageContentsAdapter.ContentVH>(diff) {
    companion object {
        private val diff = object : ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem.uri == newItem.uri && oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentVH =
        ContentVH(VhContentImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ContentVH, position: Int) {
        currentList[position]?.let {
            holder.bind(it)
        }
    }

    inner class ContentVH(
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
}

class VideoContentsAdapter : ListAdapter<Video, VideoContentsAdapter.ContentVH>(diff) {
    companion object {
        private val diff = object : ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem.uri == newItem.uri && oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentVH =
        ContentVH(VhContentVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ContentVH, position: Int) {
        currentList[position]?.let {
            holder.bind(it)
        }
    }

    inner class ContentVH(
        private val binding: VhContentVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content: Video?) {
            content?.let {
                binding.apply {
                    exoplayer.apply {
                        layoutParams.height = ((binding.root.context.getScreenWidth()?.toFloat() ?: 0f) * (9f / 16f)).toInt()
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
}