package com.gps_alarm.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bowoon.android.gps_alarm.R
import com.bowoon.android.gps_alarm.databinding.DialogConfirmDismissBinding
import com.gps_alarm.base.BaseDialog
import kotlinx.coroutines.launch

class GpsAlarmDialog(
    private val message: String,
    private val confirmText: String,
    private val confirmCallback: (() -> Unit)? = null,
    private val dismissText: String? = null,
    private val dismissCallback: (() -> Unit)? = null
) : BaseDialog<DialogConfirmDismissBinding>(
    R.layout.dialog_confirm_dismiss, true, false
) {
    private val viewModel by viewModels<GpsAlarmDialogVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = this@GpsAlarmDialog
            vm = viewModel
        }
        lifecycle.addObserver(viewModel)

        viewModel.init(message, confirmText, dismissText)

        initFlow()
    }

    override fun onBackPressed(): Boolean {
        dismissAllowingStateLoss()
        return true
    }

    private fun initFlow() {
        lifecycleScope.launch {
            viewModel.choose.collect { choose ->
                when (choose) {
                    true -> {
                        confirmCallback?.invoke()
                        dismissAllowingStateLoss()
                    }
                    false -> {
                        dismissCallback?.invoke()
                        dismissAllowingStateLoss()
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        const val TAG = "CustomDialog"
    }
}