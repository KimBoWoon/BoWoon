package com.bowoon.lol.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bowoon.commonutils.Log
import com.bowoon.lol.R
import com.bowoon.lol.base.BaseFragment
import com.bowoon.lol.databinding.FragmentSettingBinding
import com.bowoon.lol.ui.activities.vm.MainVM
import com.bowoon.lol.ui.adapter.VersionAdapter
import com.bowoon.lol.ui.dialog.LolDialog
import com.bowoon.lol.ui.fragments.vm.SettingVM
import com.bowoon.commonutils.DataStatus
import com.data.util.ViewAdapter.onDebounceClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting
) {
    private val activityVM by activityViewModels<MainVM>()
    private val viewModel by viewModels<SettingVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SettingFragment
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
            bShowDialog.onDebounceClickListener {
                LolDialog("Hello, World!", "확인", {}, "취소", {}).show(childFragmentManager, SettingFragment::class.simpleName)
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            activityVM.lolVersionList.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                    }
                    is DataStatus.Success -> {
                        binding.spinnerVersion.apply {
                            adapter = VersionAdapter(requireContext(), R.layout.spinner_item, it.data)
                            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    Log.d("data >>>>> ${it.data[position]}, position >>>>> $position")
                                    lifecycleScope.launch {
                                        viewModel.setVersion(it.data[position])
                                    }
                                }

                                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                            }

                            val version = viewModel.getVersion()
                            val versionIndex = it.data.indexOf(version)
                            if (versionIndex != -1) {
                                setSelection(versionIndex)
                            }
                        }
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }
    }
}