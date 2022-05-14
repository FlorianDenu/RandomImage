package com.example.randomimage.imagelist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.randomimage.R
import com.example.randomimage.ui.theme.RandomImageTheme
import com.example.randomimage.ui.theme.RandomImageTypography

@Composable
fun ImageListScreen(
    modifier: Modifier = Modifier,
    randomImageViewModel: RandomImageViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val error = getErrorLifecycleAware(randomImageViewModel, lifecycleOwner)
    val savedImage = getImageLifecycleAware(randomImageViewModel, lifecycleOwner)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        when {
            error.isNotEmpty() -> Message(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                text = error
            )
            savedImage.isEmpty() -> Message(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                text = stringResource(id = R.string.empty_list_text)
            )
            else -> LazyColumn(Modifier.fillMaxSize()) {
                items(items = savedImage) {
                    ImageAndAuthor(
                        Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp), imageListDataObject = it
                    )
                }
            }
        }
        AddNewImageFloatingButton {
            randomImageViewModel.fetchAllImageAndInsertRandom()
        }
    }
}

@Composable
fun ImageAndAuthor(modifier: Modifier = Modifier, imageListDataObject: ImageListDataObject) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            style = RandomImageTypography.h2,
            textAlign = TextAlign.Center,
            maxLines = 2,
            softWrap = true,
            text = stringResource(id = R.string.image_author, imageListDataObject.author)
        )
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageListDataObject.downloadUrl)
                .crossfade(true)
                .build(),
            alignment = Alignment.Center,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            },
            contentDescription = stringResource(R.string.image_content_description),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun AddNewImageFloatingButton(
    onClick: (() -> Unit)
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(end = 32.dp, bottom = 32.dp)
    ) {
        Icon(Icons.Filled.Add, stringResource(id = R.string.add_new_image_content_description))
    }
}

@Composable
fun Message(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Justify,
        style = RandomImageTypography.h1,
    )
}

@Composable
fun getErrorLifecycleAware(
    randomImageViewModel: RandomImageViewModel,
    lifecycleOwner: LifecycleOwner
): String {
    val errorStateFlow = randomImageViewModel.error
    val errorStateLifecycleAware = remember(errorStateFlow, lifecycleOwner) {
        errorStateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val error by errorStateLifecycleAware.collectAsState(initial = "")
    return error
}

@Composable
fun getImageLifecycleAware(
    randomImageViewModel: RandomImageViewModel,
    lifecycleOwner: LifecycleOwner
): List<ImageListDataObject> {
    val savedImageFlow = randomImageViewModel.savedImage
    val savedImageFlowLifecycleAware = remember(savedImageFlow, lifecycleOwner) {
        savedImageFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val savedImage by savedImageFlowLifecycleAware.collectAsState(emptyList())
    return savedImage
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomImageTheme {
        ImageAndAuthor(Modifier.fillMaxSize(), ImageListDataObject("Florian Denu", ""))
    }
}