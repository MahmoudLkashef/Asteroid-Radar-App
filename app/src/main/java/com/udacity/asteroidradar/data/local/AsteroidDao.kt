package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.model.Asteroid

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list:List<Asteroid>)

    @Query("SELECT * FROM asteroid_radar_table ORDER BY close_approach_date")
    fun getLatestData():LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_radar_table ORDER BY close_approach_date")
    fun getAllData():List<Asteroid>

    @Query("SELECT * FROM asteroid_radar_table WHERE close_approach_date = :date")
    suspend fun getDataByDate(date: String):List<Asteroid>

    @Query("SELECT * FROM asteroid_radar_table WHERE close_approach_date BETWEEN :startDate AND :endDate ORDER BY close_approach_date")
    suspend fun getDataOfWeek(startDate:String,endDate:String):List<Asteroid>

    @Query("DELETE FROM asteroid_radar_table WHERE close_approach_date= :date")
    suspend fun deletePreviousDay(date:String)

    @Query("DELETE FROM asteroid_radar_table")
    suspend fun deleteAll()
}