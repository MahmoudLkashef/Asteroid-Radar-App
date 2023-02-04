package com.udacity.asteroidradar.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repository.MainRepository
import com.udacity.asteroidradar.utils.Resource

class UpdateDataWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val TAG="UpdateDataWorker"
    companion object{
        val WORK_NAME="UpdateDataWorker"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        Log.i(TAG, "doWork: called ")
        val repository=MainRepository(context)
        return try {
            val response=repository.updateAsteroidData()
            if(response is Resource.Success){
                Result.success()
            } else{
                 Result.retry()
            }
        }catch (e:Exception){
             Result.retry()
        }
    }
}