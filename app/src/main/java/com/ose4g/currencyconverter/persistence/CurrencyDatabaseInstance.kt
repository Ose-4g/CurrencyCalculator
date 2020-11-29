package com.ose4g.currencyconverter.persistence

import android.content.Context
import androidx.room.Room

object CurrencyDatabaseInstance {
    private lateinit var db:CurrencyDatabase

    fun getInstance(applicationContext: Context):CurrencyDatabase
    {
        //Get instance of database
        if((!this::db.isInitialized))
        {
            db = Room.databaseBuilder(
                applicationContext,
                CurrencyDatabase::class.java, CurrencyDatabase.databaseName
            ).build()
        }

        return db

    }

}