package com.ose4g.currencyconverter.networkRequests

import com.ose4g.currencyconverter.models.ConvertResponse
import com.ose4g.currencyconverter.models.SymbolsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ConverterRequests {

    @GET("symbols")
    fun getAllSymbols(@Query("access_key") key:String):Call<SymbolsResponse>

    @GET("convert")
    fun getConversion(@Query("access_key") key:String,
                      @Query("from") from:String,
                      @Query("to") to:String,
                      @Query("amount") amount:Int):Call<ConvertResponse>
}