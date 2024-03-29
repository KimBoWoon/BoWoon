package com.bowoon.lol.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
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
import com.bowoon.lol.R
import com.bowoon.commonutils.ScreenUtils.dp

open class BaseDialog<V : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val fullWidth: Boolean,
    private val fullHeight: Boolean,
    private val isBackPress: Boolean = true
) : AppCompatDialogFragment() {
    protected lateinit var binding: V

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DataBindingUtil.inflate<V>(inflater, layoutId, container, false)?.apply {
            binding = this
        } ?: run {
            throw RuntimeException("BaseDialog onCreateView layout inflater error!")
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.let { window ->
            if (fullWidth && fullHeight) {
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.WHITE
                }
            } else if (fullWidth) {
                val back = ColorDrawable(Color.TRANSPARENT)
                val inset = InsetDrawable(back, 20.dp)
                window.setBackgroundDrawable(inset)

                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    when (fullHeight) {
                        true -> ViewGroup.LayoutParams.MATCH_PARENT
                        false -> ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                )
            } else {
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_BoWoon)
        return dialog
    }

    open fun onBackPressed(): Boolean {
        return isBackPress
    }
}