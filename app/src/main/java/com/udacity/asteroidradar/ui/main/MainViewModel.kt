package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repository.MainRepository
import com.udacity.asteroidradar.utils.Formatter
import com.udacity.asteroidradar.utils.Resource
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    private val TAG = "viewModel"

    private val mainRepository: MainRepository by lazy { MainRepository(app) }

    val latestData: LiveData<List<Asteroid>> =mainRepository.getLatestData()

    private val _allData = MutableLiveData<List<Asteroid>>()
    val allData: LiveData<List<Asteroid>> get() = _allData

    private val _weekData = MutableLiveData<List<Asteroid>>()
    val weekData: LiveData<List<Asteroid>> get() = _weekData

    private val _todayData = MutableLiveData<List<Asteroid>>()
    val todayData: LiveData<List<Asteroid>> get() = _todayData

    private val _pictureOfDay = MutableLiveData<String>()
    val pictureOfDay: LiveData<String> get() = _pictureOfDay

    private val _imageTitle = MutableLiveData<String>()
    val imageTitle: LiveData<String> get() = _imageTitle


    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = Formatter.formatDateToDay(LocalDateTime.now())

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = Formatter.formatDateToDay(LocalDateTime.now().plusDays(6))

    private val imageFailureMessage = "Error loading this image"

       init {
           checkInternetConnectivity()
       }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAsteroidData() {
        viewModelScope.launch {
            val response = mainRepository.updateAsteroidData()
            asteroidResponseHandler(response)
        }
    }
    fun getAllData() {
        viewModelScope.launch {
           var data = mainRepository.getAllData()
            _allData.value=data
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayData() {
        viewModelScope.launch {
            val data = mainRepository.getTodayData(startDate)
            _todayData.value = data
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekData() {
        viewModelScope.launch {
            val data = mainRepository.getDataOfWeek(startDate, endDate)
            _weekData.value = data
        }
    }

    fun getImageOfDay() {
        viewModelScope.launch {
            val response = mainRepository.getImageOfDay()
            Log.i(TAG, "getImageOfDay: ${response.data.toString() + " | " + response.message}")
            imageResponseHandler(response)
        }
    }

    private fun imageResponseHandler(response: Resource<PictureOfDay>) {
        when (response) {
            is Resource.Success -> {
                _pictureOfDay.value = response.data?.url
                _imageTitle.value = response.data?.title
                Log.i(TAG, "imageResponseHandler: ${imageTitle.value}")
            }
            is Resource.Error -> {
                _imageTitle.value = imageFailureMessage
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun asteroidResponseHandler(response: Resource<out Any>) {
        when (response) {
            is Resource.Error -> {
                Log.i(TAG, "asteroidResponseHandler: ${response.message}")
            }
        }
    }

    private fun checkInternetConnectivity() {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onAvailable(network: Network) {
                getImageOfDay()
                updateAsteroidData()
            }

            override fun onLost(network: Network) {
                Log.i(TAG, "onLost: ")
            }
        }
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
    }

}