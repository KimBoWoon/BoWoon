package com.lol.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bowoon.lol.R
import com.bowoon.lol.databinding.BottomChampionInfoBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lol.base.BaseBottomSheetDialog
import com.lol.ui.dialog.vm.ChampionInfoBottomDialogVM
import util.ContextUtils.getScreenHeight

class ChampionInfoBottomDialog(
    private val content: String
) : BaseBottomSheetDialog<BottomChampionInfoBinding>(R.layout.bottom_champion_info) {
    private val viewModel by viewModels<ChampionInfoBottomDialogVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@ChampionInfoBottomDialog
            tvContent.text = content
        }
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener { dialogInterface ->
                (dialogInterface as? BottomSheetDialog)?.run {
                    setupRatio(this)
                }
            }
        }
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        (bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) as? View)?.apply {
            BottomSheetBehavior.from(this).run {
                layoutParams.run {
                    height = getBottomSheetDialogDefaultHeight()
                    layoutParams = this
                }
//                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    /**
     * BottomSheetDialog height 설정
     */
    private fun getBottomSheetDialogDefaultHeight(percent: Int = 60): Int = (context?.getScreenHeight() ?: 0) * percent / 100
}