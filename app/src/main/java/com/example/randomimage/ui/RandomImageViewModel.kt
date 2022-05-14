package com.example.randomimage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomimage.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomImageViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            imageRepository.deleteAll()
        }
    }

    val savedImage: Flow<List<ImageListDataObject>> = imageRepository.getSavedImages().transform {
        emit(
            it.map { imageData ->
            ImageListDataObject.from(imageData)
        })
    }

    fun fetchAllImageAndInsertRandom() = viewModelScope.launch {
        imageRepository.fetchAllImageAndInsertRandom()
    }
}