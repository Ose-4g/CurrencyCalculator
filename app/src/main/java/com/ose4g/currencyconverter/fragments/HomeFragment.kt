package com.ose4g.currencyconverter.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ose4g.currencyconverter.ConverterViewModel
import com.ose4g.currencyconverter.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.lang.Exception


class HomeFragment : Fragment() {

    lateinit var mView: View
    lateinit var viewModel: ConverterViewModel
    lateinit var allSymbols:ArrayList<String>
    lateinit var spinnerAdapter:ArrayAdapter<String>
    lateinit var mHandler: Handler
    lateinit var mRunnable1:Runnable
    lateinit var mRunnable2:Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ConverterViewModel::class.java)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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



        mHandler = Handler(Looper.getMainLooper())
        mRunnable1 = Runnable {
            scaleView(hamburger, 1.0.toFloat(), 1.1.toFloat())
            mHandler.postDelayed(mRunnable2, 500)
        }

        mRunnable2 = Runnable {
            scaleView(hamburger, 1.1.toFloat(), 1.0.toFloat())
            mHandler.postDelayed(mRunnable1, 500)
        }





        mView.hamburger.setOnClickListener{
            viewModel.viewPager2.setCurrentItem(1, true)
            try {
                mHandler.removeCallbacksAndMessages(null)
            }
            catch (e:Exception)
            {

            }

        }





        viewModel.getAllCurrencies(requireContext(), requireActivity().getString(R.string.api_key))

        viewModel.allCurrency.observe(requireActivity(),
            { currencies ->
                allSymbols = ArrayList()
                for (i in currencies) {
                    Log.i("valuex", i.toString())
                    allSymbols.add(i.symbol)
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


                spinner_other_currency.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        other_currency_symbol.text = p0!!.selectedItem.toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        other_currency_symbol.text = p0!![0].toString()
                    }
                }

                spinner_base_currency.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        base_currency_symbol.text = p0!!.selectedItem.toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        base_currency_symbol.text = p0!![0].toString()
                    }
                }

            })



        mView.convert.setOnClickListener {

            mRunnable1.run()
            if(base_currency_symbol.text.toString() != other_currency_symbol.text.toString())
            {
                viewModel.convertAmount(
                    requireContext(), getString(R.string.api_key),
                    base_currency_symbol.text.toString(),
                    other_currency_symbol.text.toString()
                )

                viewModel.getLast90rates(
                    requireContext(), getString(R.string.api_key),
                    "${base_currency_symbol.text},${other_currency_symbol.text}"
                )

                val amount = base_currency_value.text.toString().toInt()
                viewModel.currencyOne.observe(requireActivity(), { currencyOne ->
                    viewModel.currencyTwo.observe(requireActivity(), { currencyTwo ->
                        Log.i(
                            "convert currency",
                            "${currencyOne.rate} ${currencyOne.symbol} = ${currencyTwo.rate} ${currencyTwo.symbol}"
                        )
                        val answer = amount * currencyTwo.rate / currencyOne.rate
                        Log.i(
                            "convert currency",
                            "$amount ${currencyOne.symbol} = $answer ${currencyTwo.symbol}"
                        )
                        other_currency_value.text = answer.toString()
                    })
                })

            }




        }





        }
    }

        fun scaleView(v: View, startScale: Float, endScale: Float) {
            val anim: Animation = ScaleAnimation(
                startScale, endScale,  // Start and end values for the X axis scaling
                startScale, endScale,  // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f
            ) // Pivot point of Y scaling
            anim.fillAfter = true // Needed to keep the result of the animation
            anim.duration = 500
            v.startAnimation(anim)
}