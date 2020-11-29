package com.ose4g.currencyconverter.persistence


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = CurrencyDatabase.tableName ,indices = arrayOf(Index(value=["symbol"],unique = true)),)
data class Currency(
    @ColumnInfo(name = "symbol") var symbol:String,
    @ColumnInfo(name="rate") var rate:Double
)
{
    @PrimaryKey(autoGenerate = true) var Id:Int?=null
    override fun toString(): String {
        return "$symbol------$rate"
    }
}