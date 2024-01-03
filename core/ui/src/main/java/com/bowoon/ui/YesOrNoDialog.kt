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
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.bowoon.commonutils.ContextUtils.getScreenWidth
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.ViewAdapter.onDebounceClickListener
import com.bowoon.commonutils.getSafetyParcelable
import com.bowoon.ui.databinding.YesOrNoDialogBinding

class YesOrNoDialog : DialogFragment() {
    companion object {
        private const val MESSAGE = "MESSAGE"
        private const val LISTENER = "LISTENER"
        private const val WIDTH = "WIDTH"
        private const val HEIGHT = "HEIGHT"
        private const val IS_CANCELABLE = "IS_CANCELABLE"
        private const val IS_CANCELED_ON_TOUCH_OUTSIDE = "IS_CANCELED_ON_TOUCH_OUTSIDE"

        fun newInstance(
            message: String,
            leftBtnText: String? = null,
            leftBtnLambda: (() -> Unit)? = null,
            rightBtnText: String? = null,
            rightBtnLambda: (() -> Unit)? = null,
            width: Int? = null,
            height: Int = LayoutParams.WRAP_CONTENT,
            isCancelable: Boolean = false,
            isCanceledOnTouchOutside: Boolean = false
        ): YesOrNoDialog = YesOrNoDialog().apply {
            arguments = bundleOf(
                MESSAGE to message,
                LISTENER to YesOrNoDialogListener().apply {
                    this@apply.leftBtnText = leftBtnText
                    this@apply.leftBtnLambda = leftBtnLambda
                    this@apply.rightBtnText = rightBtnText
                    this@apply.rightBtnLambda = rightBtnLambda
                },
                WIDTH to width,
                HEIGHT to height,
                IS_CANCELABLE to isCancelable,
                IS_CANCELED_ON_TOUCH_OUTSIDE to isCanceledOnTouchOutside
            )
        }
    }

    private var binding: YesOrNoDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = YesOrNoDialogBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            isCancelable = arguments?.getBoolean(IS_CANCELABLE, false) ?: isCancelable

            setCancelable(isCancelable)
            setCanceledOnTouchOutside(isCancelable)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            binding?.apply {
                message = it.getString(MESSAGE)
                it.getSafetyParcelable<YesOrNoDialogListener>(LISTENER)?.let {
                    leftBtnText = it.leftBtnText
                    tvLeftBtn.onDebounceClickListener { _ ->
                        it.leftBtnLambda?.invoke()
                        dismissAllowingStateLoss()
                    }
                    rightBtnText = it.rightBtnText
                    tvRightBtn.onDebounceClickListener { _ ->
                        it.rightBtnLambda?.invoke()
                        dismissAllowingStateLoss()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            arguments?.getInt(WIDTH)?.let {
                if (it == 0) {
                    (0.8 * (context?.getScreenWidth()?.toFloat() ?: 0f)).toInt()
                } else {
                    it
                }
            } ?: (0.8 * (context?.getScreenWidth()?.toFloat() ?: 0f)).toInt(),
            arguments?.getInt(HEIGHT, LayoutParams.WRAP_CONTENT) ?: LayoutParams.WRAP_CONTENT
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
                if (context.supportFragmentManager.findFragmentByTag(YesOrNoDialog::class.java.simpleName) == null) {
                    show(context.supportFragmentManager, YesOrNoDialog::class.java.simpleName)
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