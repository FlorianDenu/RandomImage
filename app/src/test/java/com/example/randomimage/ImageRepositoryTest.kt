package com.example.randomimage

import app.cash.turbine.test
import com.example.randomimage.api.ImageDataResponse
import com.example.randomimage.api.ImageService
import com.example.randomimage.database.ImageDao
import com.example.randomimage.repository.ImageRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ImageRepositoryTest {

    @MockK
    lateinit var imageService: ImageService

    @MockK
    lateinit var imageDao: ImageDao

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `given response is success when body is not null then image dao insert is called once`() =
        runBlocking {
            val imageRepository = ImageRepository(imageService, imageDao)
            coEvery { imageService.getAllPicture() } returns Response.success(mockedList)

            imageRepository.fetchAllImageAndInsertRandom()

            coVerify(exactly = 1) { imageDao.insert(any()) }
        }

    @Test
    fun `given response is cached then get all picture is not called but insert is called once`() =
        runBlocking {
            val imageRepository = ImageRepository(imageService, imageDao)
            imageRepository.imageDataList = mockedList
            coEvery { imageService.getAllPicture() } returns Response.success(mockedList)

            imageRepository.fetchAllImageAndInsertRandom()

            coVerify(exactly = 1) { imageDao.insert(any()) }
            coVerify { imageService.getAllPicture() wasNot Called }
        }

    @Test
    fun `given response is error then image dao insert is called and error message is not null`() =
        runBlocking {
            val imageRepository = ImageRepository(imageService, imageDao)
            coEvery { imageService.getAllPicture() } returns Response.error(400, mockk(relaxed = true))

            imageRepository.fetchAllImageAndInsertRandom()

            coVerify(exactly = 0) { imageDao.insert(any()) }
            imageRepository.errorStateFlow.test {
                assertTrue(awaitItem().isNotEmpty())
                cancelAndConsumeRemainingEvents()
            }
        }

    private val mockedList = listOf<ImageDataResponse>(
        mockk(relaxed = true),
        mockk(relaxed = true),
        mockk(relaxed = true)
    )
}