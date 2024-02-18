package com.bowoon.fileprovider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.getSafetyParcelableExtra
import com.bowoon.fileprovider.databinding.ActivityContentsBinding
import com.bowoon.fileprovider.databinding.VhContentBinding
import com.bowoon.mediastore.Image
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@ContentsActivity
        }

        intent.getSafetyParcelableExtra<ChooseItemList>(CONTENTS)?.run {
            this.list?.let { imageList = it }
        }

        initBinding()
    }

    private fun initBinding() {
        binding.apply {
            rvContentsList.apply {
                adapter = ContentsAdapter().apply {
                    submitList(imageList)
                }
            }
        }
    }
}

class ContentsAdapter : ListAdapter<Image, ContentsAdapter.ContentVH>(diff) {
    companion object {
        private val diff = object : ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem.uri == newItem.uri && oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentVH =
        ContentVH(VhContentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ContentVH, position: Int) {
        currentList[position]?.let {
            holder.bind(it)
        }
    }

    inner class ContentVH(
        private val binding: VhContentBinding
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