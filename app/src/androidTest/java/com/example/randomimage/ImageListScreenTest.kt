package com.example.randomimage

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.randomimage.imagelist.ImageListScreen
import com.example.randomimage.imagelist.RandomImageViewModel
import com.example.randomimage.repository.ImageRepository
import com.example.randomimage.ui.theme.RandomImageTheme
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


// Was not able to make it work, always got an error
@RunWith(AndroidJUnit4::class)
class ImageListScreenTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    @MockK(relaxUnitFun = true)
    lateinit var repository: ImageRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true, relaxUnitFun = true)
    }

    @Test
    fun addImageTest() {
        val viewModel = RandomImageViewModel(repository)
        composeTestRule.setContent {
            RandomImageTheme {
                ImageListScreen(modifier = Modifier, viewModel)
            }
        }
        composeTestRule.onAllNodes(hasContentDescription("Add new image"))[0].performClick()
    }
}