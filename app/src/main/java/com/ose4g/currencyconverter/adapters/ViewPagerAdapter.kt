package com.ose4g.currencyconverter.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

//class for main page
class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle,list:ArrayList<Fragment>)
    : FragmentStateAdapter(fragmentManager,lifecycle) {

    private val mLIst = list

    override fun getItemCount(): Int {
        return mLIst.size
    }

    override fun createFragment(position: Int): Fragment {
        return mLIst[position]
    }
}