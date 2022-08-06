package com.lol.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lol.R
import com.lol.base.BaseDialog
import com.lol.databinding.DialogConfirmDismissBinding
import com.lol.ui.dialog.vm.LolDialogVM
import kotlinx.coroutines.launch

class LolDialog(
    private val message: String,
    private val confirmText: String,
    private val confirmCallback: (() -> Unit)? = null,
    private val dismissText: String? = null,
    private val dismissCallback: (() -> Unit)? = null
) : BaseDialog<DialogConfirmDismissBinding>(
    R.layout.dialog_confirm_dismiss, false, false
) {
    private val viewModel by viewModels<LolDialogVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@LolDialog
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