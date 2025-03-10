package com.example.marsphotos.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.marsphotos.data.DaoApiRate
import com.example.marsphotos.model.ApiRate
import com.example.marsphotos.network.DatabaseProvider
import com.example.marsphotos.network.MarsApi
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date

class SyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val listResult = MarsApi.retrofitService.getPhotos()
            val exchangeRateDao = DatabaseProvider.getDatabase(applicationContext).exchangeRateDao()
            val timestamp = System.currentTimeMillis()
            val fecha= Date(timestamp)
            val formato= SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val fechaCompleta=formato.format(fecha)
            listResult.conversionRates.forEach { (targetCode, rate) ->
                exchangeRateDao.insert(
                    ApiRate(
                    baseCode = listResult.baseCode,
                    targetCode = targetCode,
                    rate = rate,
                    timestamp = timestamp
                )
                )
            }
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
