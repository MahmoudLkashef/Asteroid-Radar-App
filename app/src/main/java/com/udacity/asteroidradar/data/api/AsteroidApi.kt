package com.udacity.asteroidradar.data.api

import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApi{

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidData(
        @Query("start_date")startDate:String,
        @Query("end_date")endDate:String,
        @Query("api_key")apiKey:String
    ):Response<String>

    @GET("planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key")apiKey: String
    ):Response<PictureOfDay>
}

