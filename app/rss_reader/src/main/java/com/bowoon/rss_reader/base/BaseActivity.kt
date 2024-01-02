package com.bowoon.rss_reader.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseActivity<VIEW: ViewDataBinding>(
    @LayoutRes protected val layout: Int
) : AppCompatActivity() {
    protected val binding: VIEW by lazy {
        DataBindingUtil.setContentView(this, layout)
    }
}