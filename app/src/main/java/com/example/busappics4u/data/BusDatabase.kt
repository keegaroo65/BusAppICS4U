package com.example.busappics4u.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Trip::class], version = 1, exportSchema = false)
abstract class BusDatabase: RoomDatabase() {
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var Instance: BusDatabase? = null

        fun getDatabase(context: Context): BusDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BusDatabase::class.java, "bus_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}