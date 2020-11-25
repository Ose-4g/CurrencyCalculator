package com.ose4g.currencyconverter.models

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SymbolsResponse {
    @SerializedName("success")
    @Expose
    var success:Boolean? = null

    @SerializedName("symbols")
    @Expose
    var symbols:MutableMap<String,String> = mutableMapOf()

    fun printAll()
    {
        for(i in symbols)
            Log.i("values","$i ----${symbols[i]}")
    }

}
