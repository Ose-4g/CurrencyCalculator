package com.ose4g.currencyconverter.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConvertResponse {
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

}