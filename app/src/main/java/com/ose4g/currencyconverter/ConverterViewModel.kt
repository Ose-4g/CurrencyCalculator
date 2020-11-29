package com.ose4g.currencyconverter

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.jjoe64.graphview.series.DataPoint
import com.ose4g.currencyconverter.models.ConvertResponse
import com.ose4g.currencyconverter.networkRequests.ConverterRequests
import com.ose4g.currencyconverter.networkRequests.RetrofitHelper
import com.ose4g.currencyconverter.persistence.Currency
import com.ose4g.currencyconverter.persistence.CurrencyDatabaseInstance
import com.ose4g.currencyconverter.sharedPreference.LastSyncedSP
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ConverterViewModel :ViewModel()
{
    lateinit var viewPager2: ViewPager2//refernce to viewpager in the main activity
    lateinit var allCurrency:MutableLiveData<List<Currency>>//all curencies and their rates on the api
    lateinit var updated:MutableLiveData<Boolean>//posts true when database updates data
    lateinit var insertPosition:MutableLiveData<Long>//posts the position where a currency was inserted
    lateinit var currencyOne:MutableLiveData<Currency>//base currency
    lateinit var currencyTwo:MutableLiveData<Currency>//other currency
    lateinit var last90Rates:MutableLiveData<List<DataPoint>>//list of points for the graph
    lateinit var all90points:ArrayList<DataPoint>//works with the above
    var isInitialized = MutableLiveData<Boolean>()//checks if last90rates is initialized






    fun convertAmount(context: Context,key:String,baseCurrency: String,otherCurrency: String)
    {
        //converts base don the value in the db
        currencyOne = MutableLiveData()
        currencyTwo = MutableLiveData()
        try {
            readOneCurrencyDb(context,baseCurrency,currencyOne)
            readOneCurrencyDb(context,otherCurrency,currencyTwo)
        }
        catch (e:Exception)
        {
            Toast.makeText(context,"Please check your data connection",Toast.LENGTH_SHORT).show()
        }

    }

    fun getAllCurrencies(context: Context, key:String)
    {

        allCurrency = MutableLiveData()

        //does that only when the last updated time is less than an hour
        if(System.currentTimeMillis()-LastSyncedSP.getLastSynced(context)>3600000)
            getRemoteCurrency(context,key)
        else
            readAllFromDb(context)
    }


    private fun getRemoteCurrency(context: Context, key: String) :List<Currency>
    {
        //gets all currencies from the api and updates te rates in the db
        var list = listOf<Currency>()
        viewModelScope.launch {
            val request = RetrofitHelper.getInstance().create(ConverterRequests::class.java)

            val response = request.getAllRates(key)
            response.enqueue(object:Callback<ConvertResponse>{

                override fun onResponse(call: Call<ConvertResponse>,
                    response: Response<ConvertResponse>) {
                    if(response.isSuccessful)
                    {
                        LastSyncedSP.updateLastSynced(context,System.currentTimeMillis())
                        for(i in response.body()!!.getCurrencyList())
                        {
                            Log.i("currencies",i.toString())
                            try {
                                insertToDb(context,i)
                            }
                            catch (e:Exception)
                            {
                                Log.i("inserting-error",e.toString())
                                updateDb(context,i)
                            }

                        }
                       readAllFromDb(context)
                    }
                    else
                    {
                        try {
                            readAllFromDb(context)
                        }
                        catch (e:Exception)
                        {
                            Log.i("list-error",e.toString())
                            Toast.makeText(context,"Counldnt reas from db. Be sure you have data connected",Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ConvertResponse>, t: Throwable) {
                    try {
                        readAllFromDb(context)
                    }
                    catch (e:Exception)
                    {
                        Log.i("list-error",e.toString())
                        Toast.makeText(context,"Counldnt reas from db. Be sure you have data connected",Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        return list
    }







    fun insertToDb(context: Context, currency: Currency)
    {
        //inserts a currency into the db
        insertPosition = MutableLiveData()
        var l:Long = -10
        viewModelScope.launch {
          l=  CurrencyDatabaseInstance.getInstance(context)
                .currencyDAO().addCurrency(currency)
            insertPosition.postValue(l)
            Log.i("insert","inserted ${currency.symbol} successfully")
        }
    }



     fun updateDb(context: Context, currency: Currency)
    {
        //inserts a value into the db
        updated = MutableLiveData()
        viewModelScope.launch {
            CurrencyDatabaseInstance.getInstance(context)
                .currencyDAO().updateRate(currency.symbol,currency.rate)
            updated.postValue(true)
            Log.i("update","updated ${currency.symbol} successfully")
        }
    }

     fun readAllFromDb(context: Context)
    {
        //reads from the db
        if(!this::allCurrency.isInitialized)
            allCurrency = MutableLiveData()
        lateinit var list:List<Currency>
        viewModelScope.launch {
            Log.i("reading","I got here")
            list=CurrencyDatabaseInstance.getInstance(context)
                .currencyDAO().getAllCurrency()
            Log.i("reading_list",list.toString())
            for(i in list)
                Log.i("reading_all",i.toString())
            allCurrency.postValue(list)
        }
    }



    fun readOneCurrencyDb(context: Context,symbol:String,data:MutableLiveData<Currency>)
    {
        // reads one symbol from the db
        lateinit var currency: Currency

        viewModelScope.launch {
            Log.i("reading","I got here")
            currency=CurrencyDatabaseInstance.getInstance(context)
                .currencyDAO().getCurrency(symbol)
            data.postValue(currency)
        }
    }





    fun getLast90rates(context: Context,key:String,symbols:String) {
        //gets all rates for the previous 90 days
        all90points = ArrayList()
        last90Rates = MutableLiveData()
        isInitialized.postValue(true)
        var timeStamp = System.currentTimeMillis()
        Log.i("timestamp",timeStamp.toString()+"")
        var success = true
        var i=90

        getRateForDate(timeStamp,key,symbols,i)

    }



    companion object{
        fun stampToDate(stamp:Long):String
        //changes a timestamp to date string in the form yyyy-mm-dd
        {

            var formatter = SimpleDateFormat("yyyy-MM-dd")
            var calendar = Calendar.getInstance()
            calendar.timeInMillis = stamp
            return formatter.format(calendar.time)
        }
    }






    fun getRateForDate(timeStamp:Long,key:String,symbols:String,number:Int)
    {
        //gets the rate for a particaular day
        //does it recursively so that the reuqests are made one after the other
        if(number==0)
        {
            last90Rates.postValue(all90points)
            return
        }

        val date = stampToDate(timeStamp)
        viewModelScope.launch {
            val request = RetrofitHelper.getInstance().create(ConverterRequests::class.java)

            val response = request.getOldRate(date,key,symbols)

            response.enqueue(object : Callback<ConvertResponse> {
                override fun onResponse(
                    call: Call<ConvertResponse>,
                    response: Response<ConvertResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val currencyList= response.body()!!.getCurrencyList()
                        all90points.add(0,DataPoint(timeStamp*1.0,currencyList[1].rate/currencyList[0].rate))
                        getRateForDate(timeStamp-(1*24*60*60*1000),key,symbols,number-1)

                        Log.i("old_rate","${response.body()!!.date}---${response.body()!!.getCurrencyList()}")
                    }
                    else
                    {
                        Log.i("old_rate","not successful")
                    }
                }

                override fun onFailure(call: Call<ConvertResponse>, t: Throwable) {
                    Log.i("old_rate","total failure")
                }
            })
        }
    }

}