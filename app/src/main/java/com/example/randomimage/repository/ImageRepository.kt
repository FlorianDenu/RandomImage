package com.example.randomimage.repository

import com.example.randomimage.api.ImageDataResponse
import com.example.randomimage.api.ImageService
import com.example.randomimage.database.ImageDao
import com.example.randomimage.database.ImageData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val imageService: ImageService,
    private val imageDao: ImageDao
) {

    // will only save the result of the api in memory to respect the following AC
    //Add functionality to persist a random item from the entries in the response and
    //add it to the bottom of a list of items.
    // My understanding is that we should only save in the DB a random element from the list
    // however i would like to avoid doing too much unnecessary network calls
    private var imageDataList: List<ImageDataResponse>? = null
    private val _errorStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = _errorStateFlow

    fun getSavedImages() = imageDao.getAll()

    suspend fun fetchAllImageAndInsertRandom() {
        if (imageDataList == null) {
            val response = imageService.getAllPicture()
            if (response.isSuccessful && response.body() != null) {
                imageDataList = response.body()
                saveRandomImage()
            } else {
                _errorStateFlow.value = response.message()
            }
        } else {
            saveRandomImage()
        }
    }

    private suspend fun saveRandomImage() {
        imageDataList?.random()?.let {
            val imageData = ImageData.from(it)
            imageDao.insert(imageData)
        }
    }

    suspend fun deleteAll() = imageDao.deleteAll()
}