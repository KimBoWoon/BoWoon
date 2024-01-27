package com.bowoon.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.bowoon.commonutils.ContextUtils.getScreenWidth
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.getSafetyParcelable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomViewBottomSheetDialog<V : ViewDataBinding> : BottomSheetDialogFragment() {
    companion object {
        private const val LAYOUT_ID = "LAYOUT_ID"
        private const val VIEW_SETTINGS = "VIEW_SETTINGS"
        private const val IS_CANCELABLE = "IS_CANCELABLE"
        private const val IS_CANCELED_ON_TOUCH_OUTSIDE = "IS_CANCELED_ON_TOUCH_OUTSIDE"

        fun <V : ViewDataBinding> newInstance(
            @LayoutRes layoutId: Int,
            viewSettings: (DialogFragment.(V) -> Unit)? = null,
            isCancelable: Boolean = false,
            isCanceledOnTouchOutside: Boolean = false
        ): CustomViewBottomSheetDialog<V> = CustomViewBottomSheetDialog<V>().apply {
            arguments = bundleOf(
                LAYOUT_ID to layoutId,
                VIEW_SETTINGS to DialogListener<V>().apply {
                    this@apply.viewSettings = viewSettings
                },
                IS_CANCELABLE to isCancelable,
                IS_CANCELED_ON_TOUCH_OUTSIDE to isCanceledOnTouchOutside
            )
        }
    }

    private var binding: V? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments?.getInt(LAYOUT_ID) ?: View.NO_ID

        return DataBindingUtil.inflate<V>(inflater, layoutId, container, false).apply {
            binding = this
        }.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            isCancelable = arguments?.getBoolean(IS_CANCELABLE, false) ?: isCancelable

            setCancelable(isCancelable)
            setCanceledOnTouchOutside(isCancelable)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getSafetyParcelable<DialogListener<V>>(VIEW_SETTINGS)?.let { listener ->
            binding?.let {
                listener.viewSettings?.invoke(this@CustomViewBottomSheetDialog, it)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            (0.8 * (context?.getScreenWidth()?.toFloat() ?: 0f)).toInt(),
            LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun getTheme(): Int = R.style.Ui_DialogTheme

    fun show(context: Context) {
        (context as? FragmentActivity)?.let {
            if (!it.isFinishing) {
                if (context.supportFragmentManager.findFragmentByTag(CustomViewBottomSheetDialog::class.java.simpleName) == null) {
                    show(context.supportFragmentManager, CustomViewBottomSheetDialog::class.java.simpleName)
                } else {
                    Log.d("YesOrNoDialog is alive!")
                }
            } else {
                Log.d("activity is finish")
            }
        } ?: run {
            Log.d("activity is not FragmentActivity")
        }
    }
}