package com.guilhermelucas.seriesdatabase.detail.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SeriesDetailPageAdapter(
    fragmentManager: FragmentManager
) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (SeriesDetailAdapterData.getByPosition(
            position
        )) {
            SeriesDetailAdapterData.OVERVIEW -> SeriesOverviewFragment()
            SeriesDetailAdapterData.EPISODES -> SeriesEpisodesFragment()
            else -> throw IllegalArgumentException("Invalid index on SeriesDetailPageAdapter")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return SeriesDetailAdapterData.getByPosition(
            position
        )?.title
    }

    override fun getCount(): Int = SeriesDetailAdapterData.values().size

    enum class SeriesDetailAdapterData(val position: Int, val title: String) {
        OVERVIEW(0, "Overview"),
        EPISODES(1, "Episodes");

        companion object {
            fun getByPosition(position: Int): SeriesDetailAdapterData? {
                return values().firstOrNull { it.position == position }
            }
        }
    }
}
