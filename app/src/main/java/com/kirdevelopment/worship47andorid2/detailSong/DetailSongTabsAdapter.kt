package com.kirdevelopment.worship47andorid2.detailSong

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kirdevelopment.worship47andorid2.detailSong.fragments.ChordFragment
import com.kirdevelopment.worship47andorid2.detailSong.fragments.EngTabFragment
import com.kirdevelopment.worship47andorid2.detailSong.fragments.RuTabFragment

class DetailSongTabsAdapter(fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> RuTabFragment()

            1 -> EngTabFragment()

            else -> ChordFragment()

        }
    }
}