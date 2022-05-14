package com.example.randomimage.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.randomimage.api.ImageDataResponse

@Entity
data class ImageData(
    @PrimaryKey val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    @ColumnInfo(name = "download_url") val downloadUrl: String
) {
    companion object {
        fun from(imageDataResponse: ImageDataResponse): ImageData = with(imageDataResponse) {
            ImageData(
                id,
                author,
                width,
                height,
                url,
                downloadUrl
            )
        }
    }
}
