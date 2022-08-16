package com.practice.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.practice.R
import com.practice.base.BaseFragment
import com.practice.databinding.FragmentSettingBinding
import com.practice.ui.dialog.PokemonDialog
import com.practice.ui.fragments.vm.SettingVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.ViewAdapter.onDebounceClickListener

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting,
) {
    private val viewModel by viewModels<SettingVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = this@SettingFragment
            vm = viewModel
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding?.apply {
            tvRemoveAllWish.text = "위시 모두 제거"
            bRemoveAllWish.onDebounceClickListener {
                PokemonDialog(
                    "위시 리스트에 저장된 모든 포켓몬들이 사라집니다.\n정말 삭제하시겠습니까?",
                    "삭제",
                    {
                        lifecycleScope.launch {
                            Toast.makeText(requireContext(), viewModel.removeAllWish(), Toast.LENGTH_SHORT).show()
                        }
                    },
                    "취소",
                    {}
                ).show(childFragmentManager, SettingFragment::class.java.simpleName)
            }
        }
    }

    override fun initFlow() {}
}