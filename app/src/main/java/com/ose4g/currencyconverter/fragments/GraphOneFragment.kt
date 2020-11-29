package com.ose4g.currencyconverter.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.ose4g.currencyconverter.ConverterViewModel
import com.ose4g.currencyconverter.R
import kotlinx.android.synthetic.main.fragment_graph_one.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class GraphOneFragment : Fragment() {

    lateinit var mView:View
    private lateinit var viewModel:ConverterViewModel
    private lateinit var series:LineGraphSeries<DataPoint>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_graph_one, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ConverterViewModel::class.java)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.i("bundle",""+arguments!!.getInt("type"))


        //show progress bar
        graphView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        //
        viewModel.isInitialized.observe(requireActivity(),
            { boolean ->
                if (boolean) {
                    //get rates from the last 90 days
                    viewModel.last90Rates.observe(requireActivity(),
                        //setting up the graphview
                        { list ->
                            series = if (arguments!!.getInt("type") == 0) {
                                LineGraphSeries(list.subList(0, 60).toTypedArray())
                            } else {
                                LineGraphSeries(list.toTypedArray())
                            }

                            for (i in list.toTypedArray())
                                Log.i("series", "${i.x}:${i.y}")

                            graphView.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            series.thickness = 1
                            series.isDrawBackground = true
                            series.color = R.drawable.gradient_transprent_background

                            graphView.removeAllSeries()
                            graphView.addSeries(series)

                            graphView.gridLabelRenderer.numHorizontalLabels = 10

                            graphView.gridLabelRenderer.horizontalLabelsColor = Color.WHITE

                            graphView.gridLabelRenderer.verticalLabelsColor = Color.WHITE

                            graphView.gridLabelRenderer.labelFormatter =
                                object : DefaultLabelFormatter() {
                                    override fun formatLabel(
                                        value: Double,
                                        isValueX: Boolean
                                    ): String {
                                        return if (isValueX) {
                                            // show normal x values
                                            ConverterViewModel.stampToDate(value.toLong())
                                        } else {
                                            // show currency for y values
                                            super.formatLabel(value, isValueX)
                                        }
                                    }
                                }

                            graphView.viewport.setMinY(0.0)
                            graphView.viewport.setMinX(series.lowestValueX)
                            graphView.viewport.setMaxX(series.highestValueX)
                            graphView.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE
                            Log.i("highest", series.highestValueY.toString())
                            graphView.viewport.setMaxY(series.highestValueY)
                            graphView.viewport.isXAxisBoundsManual = true
                            graphView.viewport.isYAxisBoundsManual = true
                            graphView.gridLabelRenderer.numVerticalLabels = 10
                            graphView.gridLabelRenderer.setHorizontalLabelsAngle(75)

                            viewModel.currencyOne.observe(requireActivity(),{
                                    currencyOne->
                                viewModel.currencyTwo.observe(requireActivity(),{
                                        currencyTwo->
                                    graphView.title = currencyOne.symbol+"--"+currencyTwo.symbol
                                    graphView.titleColor = Color.WHITE
                                })
                            })
                            graphView.background = ContextCompat.getDrawable(
                                requireContext(),
                                android.R.color.transparent
                            )
                        })
                }

            })

    }


}