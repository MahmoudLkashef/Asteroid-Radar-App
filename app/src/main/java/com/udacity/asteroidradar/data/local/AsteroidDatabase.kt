package com.udacity.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.model.Asteroid

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase:RoomDatabase() {

    abstract val asteroidDao:AsteroidDao

    companion object{
        /*
        Make sure that the value is up to date and same to all execution threads
        The value of volatile will never be cached and all read and write done to and from main memory
        It means that changes made by one tread to instance are visible to all threads immediately
        */
        @Volatile
        private var INSTANCE:AsteroidDatabase? = null

        fun getInstance(context: Context):AsteroidDatabase{
            //Multiple threads can ask for database instance at the same time leaving us with two instead of one
            //So synchronized means only one thread at execution time can enter this block of code which make sure that database get initialized once
            synchronized(this){
                var instance= INSTANCE
                if(instance==null){
                    instance=Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroid_radar_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}