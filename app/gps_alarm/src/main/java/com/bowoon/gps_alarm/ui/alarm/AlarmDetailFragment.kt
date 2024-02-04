package com.bowoon.gps_alarm.ui.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.FragmentAlarmDetailBinding
import com.bowoon.gps_alarm.base.BaseFragment
import com.bowoon.gps_alarm.data.Week

class AlarmDetailFragment : BaseFragment() {
    companion object {
        private const val TAG = "#AlarmDetailFragment"
    }

    private lateinit var binding: FragmentAlarmDetailBinding
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val alarm by lazy { navArgs<AlarmDetailFragmentArgs>().value.alarm }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        latitude = alarm?.latitude
        longitude = alarm?.longitude

        binding.apply {
            lifecycleOwner = this@AlarmDetailFragment
            alarm = this@AlarmDetailFragment.alarm
        }

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
            mapView.apply {
                latitude = this@AlarmDetailFragment.alarm?.latitude
                longitude = this@AlarmDetailFragment.alarm?.longitude
            }

            val backgroundResource = ResourcesCompat.getDrawable(resources, R.drawable.choose_day_bg, null)

            this@AlarmDetailFragment.alarm?.weekList?.forEach {
                when (it) {
                    Week.MONDAY -> includeWeek.tvMonday.background = backgroundResource
                    Week.TUESDAY -> includeWeek.tvTuesday.background = backgroundResource
                    Week.WEDNESDAY -> includeWeek.tvWednesday.background = backgroundResource
                    Week.THURSDAY -> includeWeek.tvThursday.background = backgroundResource
                    Week.FRIDAY -> includeWeek.tvFriday.background = backgroundResource
                    Week.SATURDAY -> includeWeek.tvSaturday.background = backgroundResource
                    Week.SUNDAY -> includeWeek.tvSunday.background = backgroundResource
                }
            }
        }
    }

    override fun initFlow() {}
}