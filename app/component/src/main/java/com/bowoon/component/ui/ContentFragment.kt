package com.bowoon.component.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bowoon.component.adapters.ComponentAdapter
import com.bowoon.component.data.Components
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

        mutableListOf<Components>().apply {
            items?.components?.forEach { component ->
                add(componentUtils.createComponent(component))
            }
        }.run {
            binding?.rvComponentList?.adapter = ComponentAdapter().apply {
                submitList(this@run)
            }
        }
    }
}