package com.bowoon.component.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bowoon.component.adapters.ComponentAdapter
import com.bowoon.component.data.Tab
import com.bowoon.component.databinding.FragmentContentBinding
import com.bowoon.component.utils.ComponentUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContentFragment(
    private val items: Tab? = null
) : Fragment() {
    private var binding: FragmentContentBinding? = null

    @Inject
    lateinit var componentUtils: ComponentUtils
    private val viewModel by activityViewModels<MainVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentBinding.inflate(inflater, null, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        items?.components?.map { component -> componentUtils.createComponent(component) }?.run {
            binding?.rvComponentList?.adapter = ComponentAdapter(viewModel).apply {
                submitList(this@run)
            }
        }
    }
}