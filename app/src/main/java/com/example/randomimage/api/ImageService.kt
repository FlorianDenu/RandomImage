package com.example.randomimage.api

import retrofit2.Response
import retrofit2.http.GET

interface ImageService {

    @GET("v2/list")
    suspend fun getAllPictures(): Response<List<ImageDataResponse>>
}