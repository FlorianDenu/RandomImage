package com.example.randomimage.di

import android.content.Context
import androidx.room.Room
import com.example.randomimage.api.ImageService
import com.example.randomimage.database.ImageDao
import com.example.randomimage.database.RandomImageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providePictureService(): ImageService {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ImageService::class.java)
    }

    @Provides
    fun provideImageDao(appDatabase: RandomImageDatabase): ImageDao {
        return appDatabase.imageDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): RandomImageDatabase {
        return Room.databaseBuilder(
            appContext,
            RandomImageDatabase::class.java,
            "random-image.db"
        ).build()
    }
}