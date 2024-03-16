package com.bowoon.fileprovider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.getSafetyParcelableExtra
import com.bowoon.fileprovider.databinding.ActivityContentsBinding
import com.bowoon.mediastore.ChooseItemList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentsActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "fileprovider_contents_activity"
        const val CONTENTS = "CONTENTS"
    }

    private val binding: ActivityContentsBinding by lazy {
        DataBindingUtil.setContentView(this@ContentsActivity, R.layout.activity_contents)
    }
    private val adapter = ContentsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@ContentsActivity
        }

        intent.getSafetyParcelableExtra<ChooseItemList>(CONTENTS)?.run {
            this.list?.let {
                adapter.submitList(it)
            }
        }

        initBinding()
    }

    private fun initBinding() {
        binding.apply {
            rvContentsList.apply {
                val spanCount = 1

                layoutManager = GridLayoutManager(this@ContentsActivity, spanCount, RecyclerView.VERTICAL, false)
                adapter = this@ContentsActivity.adapter
            }
        }
    }
}