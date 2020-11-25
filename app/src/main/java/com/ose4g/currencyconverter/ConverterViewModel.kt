package com.ose4g.currencyconverter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.ose4g.currencyconverter.models.ConvertResponse
import com.ose4g.currencyconverter.models.SymbolsResponse
import com.ose4g.currencyconverter.networkRequests.ConverterRequests
import com.ose4g.currencyconverter.networkRequests.RetrofitHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConverterViewModel :ViewModel()
{
    lateinit var viewPager2: ViewPager2
    lateinit var symbolsGotten:MutableLiveData<SymbolsResponse>
    lateinit var conversionDone:MutableLiveData<ConvertResponse>

    fun getAllSymbols(key:String)
    {
        symbolsGotten = MutableLiveData()
        viewModelScope.launch {
            val request = RetrofitHelper.getInstance().create(ConverterRequests::class.java)

            val response = request.getAllSymbols(key)
            response.enqueue(object :Callback<SymbolsResponse>{
                override fun onResponse(
                    call: Call<SymbolsResponse>,
                    response: Response<SymbolsResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        symbolsGotten.postValue(response.body())
                        Log.i("value","this is the first point")
                        Log.i("value","symbols = ${response.body()!!.symbols!!}")
                        response.body()!!.printAll()
                    }
                    else
                    {
                        Log.i("error","something went wrong")
                    }
                }

                override fun onFailure(call: Call<SymbolsResponse>, t: Throwable) {
                    Log.i("error","Unable to get all symbols")
                }

            })
        }

    }

    fun getSymbolsLiveData(key: String):MutableLiveData<SymbolsResponse>
    {
        if((!this::symbolsGotten.isInitialized))
            getAllSymbols(key)
        return symbolsGotten
    }

    fun convertAmount(key:String,from:String,to:String,amount:Int)
    {
        viewModelScope.launch {
            val request = RetrofitHelper.getInstance().create(ConverterRequests::class.java)

            val response = request.getConversion(key, from, to, amount)
            response.enqueue(object : Callback<ConvertResponse>{
                override fun onResponse(
                    call: Call<ConvertResponse>,
                    response: Response<ConvertResponse>
                ) {
                   if(response.isSuccessful)
                       Log.i("result",""+response.body()!!.success)
                    else
                       Log.i("result","reponse not sucessful")
                }

                override fun onFailure(call: Call<ConvertResponse>, t: Throwable) {
                    Log.i("result","reponse failed")
                }
            })
        }
    }
}