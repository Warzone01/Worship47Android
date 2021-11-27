package com.kirdevelopment.worship47andorid2.startScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kirdevelopment.worship47andorid2.startScreen.fragments.FirstFragment
import com.kirdevelopment.worship47andorid2.startScreen.fragments.SecondFragment
import com.kirdevelopment.worship47andorid2.startScreen.fragments.ThirdFragment

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> FirstFragment()

            1 -> SecondFragment()

            else -> ThirdFragment()

        }
    }
}