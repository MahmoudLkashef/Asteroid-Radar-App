package com.udacity.asteroidradar.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.data.api.AsteroidInstance
import com.udacity.asteroidradar.data.api.NetworkUtils
import com.udacity.asteroidradar.data.local.AsteroidDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.Formatter
import com.udacity.asteroidradar.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime

class MainRepository(context: Context) {
    private val TAG = "repository"

    private val database by lazy { AsteroidDatabase.getInstance(context) }

    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = Formatter.formatDateToDay(LocalDateTime.now())

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = Formatter.formatDateToDay(LocalDateTime.now().plusDays(6))

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateAsteroidData() =
        withContext(Dispatchers.IO) {
            try {
                val response = AsteroidInstance.api.getAsteroidData(startDate, endDate, BuildConfig.API_KEY)
                Log.i(TAG, "updateAsteroidData: ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val asteroidList=convertResponseToAsteroidList(response.body().toString())
                    updateLocalData(asteroidList)
                    Resource.Success(asteroidList)
                }
                else Resource.Error<List<Asteroid>>(response.message().toString(),emptyList())
            }catch (e:Exception){
                Resource.Error<String>(e.message.toString())
            }
    
        }

    suspend fun updateLocalData(list: List<Asteroid>) = withContext(Dispatchers.IO) {
        database.asteroidDao.insertAll(list)
    }

    fun getLatestData(): LiveData<List<Asteroid>> {
        return database.asteroidDao.getLatestData()
    }

    suspend fun getAllData()= withContext(Dispatchers.IO){
        database.asteroidDao.getAllData()
    }

    suspend fun getDataOfWeek(startDate: String, endDate: String) = withContext(Dispatchers.IO) {
        database.asteroidDao.getDataOfWeek(startDate, endDate)
    }

    suspend fun getTodayData(date: String) = withContext(Dispatchers.IO) {
        database.asteroidDao.getDataByDate(date)
    }

    private fun convertResponseToAsteroidList(response: String): List<Asteroid> {
        val jsonResult = JSONObject(response)
        return NetworkUtils.parseAsteroidsJsonResult(jsonResult)
    }

    suspend fun getImageOfDay() = withContext(Dispatchers.IO) {
        try {
            val response = AsteroidInstance.api.getPictureOfDay(BuildConfig.API_KEY)
            if (response.isSuccessful && response.body()!=null) {
                Resource.Success(response.body()!!)
            }
            else Resource.Error<PictureOfDay>(response.message().toString())

        }catch (e:Exception){
            Resource.Error<PictureOfDay>(e.message.toString())
        }
    }
}