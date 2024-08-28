package com.sk.trade.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class ViewPagerAdapter(
    fm: FragmentManager,
    private val list: ArrayList<Fragment>,
    private val titleList: List<String>
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(i: Int): Fragment {
        return list[i]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }
}