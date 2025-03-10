package com.example.marsphotos.network

import android.content.Context
import androidx.room.Room
import com.example.marsphotos.data.ExchangeRateDatabase

object DatabaseProvider {
    private var database: ExchangeRateDatabase? = null

    fun getDatabase(context: Context): ExchangeRateDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ExchangeRateDatabase::class.java,
                "exchange_rate_database"
            ).build()
            database = instance
            instance
        }
    }
}
