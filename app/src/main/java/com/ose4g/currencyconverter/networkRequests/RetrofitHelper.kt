package com.ose4g.currencyconverter.networkRequests

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper
{
    private lateinit var retrofit: Retrofit
    private const val BASE_URL = "http://data.fixer.io/api/"
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client =OkHttpClient.Builder().addInterceptor(interceptor)
    fun getInstance():Retrofit
    {
        if(!(this::retrofit.isInitialized))
            retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        return retrofit
    }

}