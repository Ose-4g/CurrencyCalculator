package com.ose4g.currencyconverter.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ose4g.currencyconverter.persistence.Currency

class ConvertResponse {
    //response from the api
    @SerializedName("sucesss")
    @Expose
    var success:Boolean?=null

    @SerializedName("timestamp")
    @Expose
    var timestamp:Long? = null

    @SerializedName("date")
    @Expose
    var date:String?=null

    @SerializedName("rates")
    @Expose
    var symbolsRates:Map<String,Double> = mapOf()

    fun getCurrencyList():ArrayList<Currency>
    {
        var list = ArrayList<Currency>()
        for(i in symbolsRates)
        {
            var string = i.toString().split("=")
            var currency = Currency(string[0],string[1].toDouble())
            list.add(currency)
        }
        return list
    }

}