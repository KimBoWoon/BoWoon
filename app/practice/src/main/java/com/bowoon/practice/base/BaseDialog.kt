package com.bowoon.practice.base

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.bowoon.practice.R

abstract class BaseDialog<T : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val fullWidth: Boolean,
    private val fullHeight: Boolean,
    private val isBackPress: Boolean = true
) : AppCompatDialogFragment() {
    protected lateinit var binding: T

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.let { window ->
            if (fullWidth && fullHeight) {
//                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.statusBarColor = Color.WHITE
                }
            } else if (fullWidth) {
//                val back = ColorDrawable(Color.TRANSPARENT)
//                val inset = InsetDrawable(back, 20.dp)
//                window.setBackgroundDrawable(inset)

                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    when (fullHeight) {
                        true -> ViewGroup.LayoutParams.MATCH_PARENT
                        false -> ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                )
            } else {
//                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    when (fullHeight) {
                        true -> ViewGroup.LayoutParams.MATCH_PARENT
                        false -> ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                )
            }
        }

        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@setOnKeyListener onBackPressed()
            }
            false
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_BoWoon)
            setCanceledOnTouchOutside(false)
        }

    open fun onBackPressed(): Boolean {
        return isBackPress
    }
}