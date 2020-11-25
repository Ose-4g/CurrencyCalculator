package com.ose4g.currencyconverter.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ose4g.currencyconverter.ConverterViewModel
import com.ose4g.currencyconverter.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    lateinit var mView: View
    lateinit var viewModel: ConverterViewModel
    lateinit var allSymbols:ArrayList<String>
    lateinit var spinnerAdapter:ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ConverterViewModel::class.java)

        val tv: TextView = mView.findViewById(R.id.textView_title)
        val word = "Currency\nCalculator."
        val wordSpan: Spannable = SpannableString(word)
        wordSpan.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            ), word.length - 1, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv.text = wordSpan

        mView.hamburger.setOnClickListener{
            viewModel.viewPager2.setCurrentItem(1, true)
        }



        viewModel.getSymbolsLiveData(getString(R.string.api_key)).observe(requireActivity(),
            { symbolResponse ->
                allSymbols = ArrayList()
                for (i in symbolResponse.symbols) {
                    Log.i("valuex", i.toString().split("=")[0])
                    allSymbols.add(i.toString().split("=")[0])
                }

                spinnerAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    R.id.customSpinnerId,
                    allSymbols
                )
                // spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinner_base_currency.adapter = spinnerAdapter
                spinner_other_currency.adapter = spinnerAdapter


                spinner_other_currency.onItemSelectedListener = object: OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        other_currency_symbol.text = p0!!.selectedItem.toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        other_currency_symbol.text = p0!![0].toString()
                    }
                }

                spinner_base_currency.onItemSelectedListener =  object: OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        base_currency_symbol.text = p0!!.selectedItem.toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        base_currency_symbol.text = p0!![0].toString()
                    }
                }

            })


                mView.convert.setOnClickListener {
            viewModel.convertAmount(getString(R.string.api_key),base_currency_symbol.text.toString(),
            other_currency_symbol.text.toString(),
            base_currency_value.text.toString().toInt())
        }


        return mView
    }

}