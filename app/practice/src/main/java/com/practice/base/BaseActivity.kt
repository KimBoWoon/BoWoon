package com.practice.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<V : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {
    protected val binding: V by lazy {
        DataBindingUtil.setContentView(this, layoutId)
    }

    abstract fun initBinding()
    abstract fun initFlow()
}