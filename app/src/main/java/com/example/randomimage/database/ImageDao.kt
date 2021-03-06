package com.example.randomimage.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM imageData")
    fun getAll(): Flow<List<ImageData>>

    @Query("SELECT * FROM imageData WHERE id LIKE :id LIMIT 1")
    suspend fun getImageById(id: String): ImageData

    @Insert(onConflict = IGNORE)
    suspend fun insert(imageData: ImageData)

    @Query("DELETE FROM imageData")
    suspend fun deleteAll()
}