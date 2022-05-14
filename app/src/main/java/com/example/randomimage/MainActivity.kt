package com.example.randomimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.randomimage.ui.ImageListDataObject
import com.example.randomimage.ui.RandomImageViewModel
import com.example.randomimage.ui.theme.RandomImageTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val randomImageViewModel by viewModels<RandomImageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            for (i in 0 until 5) {
                delay(1000)
                randomImageViewModel.fetchAllImageAndInsertRandom()
            }
        }

        setContent {
            RandomImageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val savedImageFlow = randomImageViewModel.savedImage

                    val lifecycleOwner = LocalLifecycleOwner.current
                    val savedImageFlowLifecycleAware = remember(savedImageFlow, lifecycleOwner) {
                        savedImageFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    }

                    val savedImage by savedImageFlowLifecycleAware.collectAsState(emptyList())

                    LazyColumn {
                        items(items = savedImage) {
                            Greeting(imageListDataObject = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(imageListDataObject: ImageListDataObject) {
    Text(text = "from ${imageListDataObject.author}!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomImageTheme {
        Greeting(ImageListDataObject("Florian Denu", ""))
    }
}