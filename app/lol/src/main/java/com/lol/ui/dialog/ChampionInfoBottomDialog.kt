package com.lol.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.lol.R
import com.lol.base.BaseBottomSheetDialog
import com.lol.databinding.BottomChampionInfoBinding
import com.lol.ui.dialog.vm.ChampionInfoBottomDialogVM

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
}