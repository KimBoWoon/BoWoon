package com.practice.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.practice.R
import com.practice.base.BaseDialog
import com.practice.databinding.DialogConfirmDismissBinding
import com.practice.ui.dialog.vm.PokemonDialogVM
import kotlinx.coroutines.launch

class PokemonDialog(
    private val message: String,
    private val confirmText: String,
    private val confirmCallback: (() -> Unit)? = null,
    private val dismissText: String? = null,
    private val dismissCallback: (() -> Unit)? = null
) : BaseDialog<DialogConfirmDismissBinding>(
    R.layout.dialog_confirm_dismiss, false, false
) {
    private val viewModel by viewModels<PokemonDialogVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@PokemonDialog
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