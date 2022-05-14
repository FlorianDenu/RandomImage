package com.example.randomimage.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageData::class], version = 1)
abstract class RandomImageDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
}