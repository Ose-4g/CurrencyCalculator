package com.ose4g.currencyconverter.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ose4g.currencyconverter.fragments.GraphOneFragment

class GraphFragmentAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       var bundle= Bundle()
        bundle.putInt("type",position)
        var fragment = GraphOneFragment()
        fragment.arguments =bundle
        return fragment
    }
}