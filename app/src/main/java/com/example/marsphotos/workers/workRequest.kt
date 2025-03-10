package com.example.marsphotos.workers


import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun scheduleDailySync(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "SyncWork",
        ExistingPeriodicWorkPolicy.REPLACE,
        syncWorkRequest
    )
}