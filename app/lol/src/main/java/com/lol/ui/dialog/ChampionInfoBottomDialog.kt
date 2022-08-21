package com.lol.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lol.R
import com.lol.databinding.BottomChampionInfoBinding
import com.lol.ui.dialog.vm.ChampionInfoBottomDialogVM


class ChampionInfoBottomDialog(
//    @LayoutRes private val layoutId: Int,
    private val content: String
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomChampionInfoBinding
    private val viewModel by viewModels<ChampionInfoBottomDialogVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<BottomChampionInfoBinding>(
            inflater,
            R.layout.bottom_champion_info,
            container,
            false
        )?.apply {
            binding = this
        } ?: run {
            throw RuntimeException("BottomSheetDialog onCreateView layout inflater error!")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvContent.text = content
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(true)
        }
}