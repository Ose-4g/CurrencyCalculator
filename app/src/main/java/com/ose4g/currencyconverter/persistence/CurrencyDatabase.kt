package com.ose4g.currencyconverter.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Currency::class],version=1)
abstract class CurrencyDatabase:RoomDatabase() {
    companion object
    {
        const val databaseName:String = "currency_db"
        const val tableName:String = "CURRENCIES"
    }
    abstract fun currencyDAO():CurrencyDAO
}