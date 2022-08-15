package com.practice.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.practice.R
import com.practice.base.BaseFragment
import com.practice.databinding.FragmentSettingBinding
import com.practice.ui.fragments.vm.SettingVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting,
) {
//    @Inject
//    lateinit var roomHelper: RoomHelper
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
        binding?.tvRemoveAllWish?.text = "위시 모두 제거"
    }

    override fun initFlow() {
//        viewModel.removeAllWish.observe(viewLifecycleOwner) {
//            PokemonDialog(
//                "위시 리스트에 저장된 모든 포켓몬들이 사라집니다.\n정말 삭제하시겠습니까?",
//                "삭제",
//                {
//                    lifecycleScope.launch(Dispatchers.IO) {
//                        roomHelper.roomPokemonDao().getWishPokemonList()?.let {
//                            val toastMessage = if (it.isEmpty()) {
//                                "위시 리스트에 저장된 포켓몬이 없습니다."
//                            } else {
//                                roomHelper.roomPokemonDao().wishDeleteAll()
//                                "위시 리스트를 모두 삭제했습니다."
//                            }
//                            withContext(Dispatchers.Main) {
//                                Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                },
//                "취소",
//                {}
//            ).show(childFragmentManager, SettingFragment::class.java.simpleName)
//        }
    }
}