package com.example.marsphotos.data

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.marsphotos.model.ApiRate


@Dao
interface DaoApiRate {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ApiRate)

    @Query("SELECT DISTINCT baseCode FROM exchange_rate")
    fun getDistinctCurrencies(): Cursor
    @Query("SELECT DISTINCT targetCode FROM exchange_rate")
    fun getDistinctTargetCurrencies(): Cursor
            @Query(
        "SELECT timestamp, rate FROM exchange_rate " +
                "WHERE baseCode = :baseCode AND targetCode = :targetCode " +
                "AND timestamp BETWEEN :startDate AND :endDate " +
                "ORDER BY timestamp ASC"
    )
    fun getRates(
        baseCode: String,
        targetCode: String,
        startDate: Long,
        endDate: Long
    ): Cursor

@Database(entities = [ApiRate::class], version = 1, exportSchema = false)
abstract class ExchangeRateDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): DaoApiRate
}
}

