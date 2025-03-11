package com.example.marsphotos.network

import android.content.Context
import androidx.room.Room
import com.example.marsphotos.data.DaoApiRate

object DatabaseProvider {
    private var database: DaoApiRate.ExchangeRateDatabase? = null

    fun getDatabase(context: Context): DaoApiRate.ExchangeRateDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                DaoApiRate.ExchangeRateDatabase::class.java,
                "exchange_rate_database"
            ).build()
            database = instance
            instance
        }
    }
}
