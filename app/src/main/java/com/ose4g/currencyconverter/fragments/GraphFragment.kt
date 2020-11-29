package com.ose4g.currencyconverter.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.ose4g.currencyconverter.R
import com.ose4g.currencyconverter.adapters.GraphFragmentAdapter
import kotlinx.android.synthetic.main.fragment_graph.*

class GraphFragment : Fragment() {

    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_graph, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf<String>(getString(R.string.title1),getString(R.string.title2))
        // setting up viewpager and tabLayout
        viewPagerGraphs.adapter = GraphFragmentAdapter(requireFragmentManager(),requireActivity().lifecycle)
        val mediator = TabLayoutMediator(tabLayout,viewPagerGraphs) { tab, position ->
            tab.text = list[position]
        }
        mediator.attach()


    }
}