package com.lol.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheetDialog<V : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : BottomSheetDialogFragment() {
    protected lateinit var binding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<V>(inflater, layoutId, container, false)?.apply {
            binding = this
        } ?: run {
            throw RuntimeException("BottomSheetDialog onCreateView layout inflater error!")
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(true)
        }
}