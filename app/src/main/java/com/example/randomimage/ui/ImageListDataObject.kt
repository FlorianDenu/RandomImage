package com.example.randomimage.ui

import com.example.randomimage.database.ImageData

data class ImageListDataObject(
    val author: String,
    val downloadUrl: String
) {
    companion object {
        fun from(imageData: ImageData): ImageListDataObject = with(imageData) {
            ImageListDataObject(
                author,
                downloadUrl
            )
        }
    }
}
