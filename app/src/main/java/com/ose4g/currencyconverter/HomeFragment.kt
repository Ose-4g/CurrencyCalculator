package com.ose4g.currencyconverter

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    lateinit var mView: View
    lateinit var viewModel: ConverterViewModel

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
        wordSpan.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(),R.color.green)),word.length-1,word.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = wordSpan

        mView.hamburger.setOnClickListener{
            viewModel.viewPager2.setCurrentItem(1,true)
        }
        return mView
    }

}