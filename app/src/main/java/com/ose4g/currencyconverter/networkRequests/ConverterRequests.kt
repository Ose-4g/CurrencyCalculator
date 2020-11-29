package com.ose4g.currencyconverter.networkRequests

import com.ose4g.currencyconverter.models.ConvertResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ConverterRequests {


    //gets all latest rates from the api
    @GET("latest")
    fun getAllRates(@Query("access_key") key:String):Call<ConvertResponse>


    //get rate for date
    @GET("{date}")
    fun getOldRate(@Path("date") date:String,
                   @Query("access_key") key:String,
                   @Query("symbols") symbols:String):Call<ConvertResponse>


}