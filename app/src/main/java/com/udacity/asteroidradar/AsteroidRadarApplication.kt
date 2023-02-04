package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.udacity.asteroidradar.worker.UpdateDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        delayInit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun delayInit() {
        applicationScope.launch {
            updateAsteroidDataWorker()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAsteroidDataWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<UpdateDataWorker>(1,TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            UpdateDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}