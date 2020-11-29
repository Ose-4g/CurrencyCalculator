package com.ose4g.currencyconverter.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ose4g.currencyconverter.MainActivity

@Dao
interface CurrencyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCurrency(currency: Currency):Long

    @Query("SELECT * FROM ${CurrencyDatabase.tableName} WHERE symbol = :symbol")
    suspend fun getCurrency(symbol:String):Currency

    @Query("SELECT * FROM ${CurrencyDatabase.tableName} ORDER BY symbol")
    suspend fun getAllCurrency():List<Currency>

    @Query("UPDATE ${CurrencyDatabase.tableName} SET rate =:rate WHERE symbol = :symbol")
    suspend fun updateRate(symbol: String,rate:Double)
}