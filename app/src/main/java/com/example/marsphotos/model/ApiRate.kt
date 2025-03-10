package com.example.marsphotos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "exchange_rate")
data class ApiRate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val baseCode: String,
    val targetCode: String,
    val rate: Double,
    val timestamp: Long
    )
