package com.example.randomimage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.randomimage.database.ImageDao
import com.example.randomimage.database.ImageData
import com.example.randomimage.database.RandomImageDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RandomImageReadWriteTest {

    private lateinit var imageDao: ImageDao
    private lateinit var db: RandomImageDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RandomImageDatabase::class.java).build()
        imageDao = db.imageDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeImageAndGetAllWithFirstInserted() = runBlocking{
        imageDao.deleteAll()
        imageDao.insert(imageData1)
        imageDao.insert(imageData2)
        val allImage = imageDao.getAll().firstOrNull()
        assertThat(allImage?.firstOrNull(), equalTo(imageData1))
        assertThat(allImage?.get(1), equalTo(imageData2))
    }

    @Test
    @Throws(Exception::class)
    fun writeSameImageAndMakeSureItDoesNotHaveDuplicate() = runBlocking{
        imageDao.deleteAll()
        imageDao.insert(imageData1)
        imageDao.insert(imageData1)
        imageDao.insert(imageData1)
        val allImage = imageDao.getAll().firstOrNull()
        assertEquals(allImage?.size, 1)
    }

    private val imageData1 = ImageData(
        "1",
        "Alejandro Escamilla",
        5616,
        3744,
        "https://unsplash.com/photos/yC-Yzbqy7PY",
        "https://picsum.photos/id/0/5616/3744"
    )

    private val imageData2 = ImageData(
        "2",
        "Alejandro Escamilla",
        5616,
        3744,
        "https://unsplash.com/photos/LNRyGwIJr5c",
        "https://picsum.photos/id/1/5616/3744"
    )
}