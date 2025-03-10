package com.example.marsphotos.data

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

    @Query("SELECT * FROM exchange_rate " +
            "WHERE baseCode = :baseCode AND targetCode = :targetCode " +
            "ORDER BY timestamp DESC")
    suspend fun getRates(baseCode: String, targetCode: String): List<ApiRate>
}

@Database(entities = [ApiRate::class], version = 1, exportSchema = false)
abstract class ExchangeRateDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): DaoApiRate
}

