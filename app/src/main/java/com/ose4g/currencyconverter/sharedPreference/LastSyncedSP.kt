package com.ose4g.currencyconverter.sharedPreference

import android.content.Context
import android.content.SharedPreferences

object LastSyncedSP
{
    lateinit var sharedPreferences:SharedPreferences
    val NAME:String  = "LastSyncedSharedPreferences"
    val LAST_SYNCED = "last_synced"

    fun getInstance(context: Context):SharedPreferences
    {
        if(!(this::sharedPreferences.isInitialized))
        {
            sharedPreferences = context.getSharedPreferences(NAME,Context.MODE_PRIVATE)
        }

        if(sharedPreferences!!.all.isEmpty())
        {
            val editor:SharedPreferences.Editor  = sharedPreferences.edit()
            editor.putLong(LAST_SYNCED,0)
            editor.apply()
        }
        return sharedPreferences
    }

    fun updateLastSynced(context: Context,timeStamp:Long)
    {
        //sets the last synced time
        var sp = getInstance(context)
        val editor = sp.edit()
        editor.putLong(LAST_SYNCED,timeStamp)
        editor.apply()
    }

    fun getLastSynced(context: Context):Long
    {
        //gets the last synced time
        return getInstance(context).getLong(LAST_SYNCED,0)
    }
}