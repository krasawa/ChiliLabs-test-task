package com.marus.clililabs_test_task.ui.details

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.marus.clililabs_test_task.R
import com.marus.clililabs_test_task.model.Gif
import com.marus.clililabs_test_task.ui.common.views.LoadingView
import com.marus.clililabs_test_task.ui.theme.CliliLabstesttaskTheme
import com.marus.clililabs_test_task.ui.theme.WhiteSemiTransparent
import com.marus.clililabs_test_task.ui.util.SampleData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftDetailsScreen(
    gifId: String,
    viewModel: GifDetailsViewModel
) {
    val gif by viewModel.gif.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    CliliLabstesttaskTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(R.string.title_gif_details)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            (context as? Activity)?.finish()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            LaunchedEffect(key1 = true) {
                viewModel.findGifById(gifId)
            }

            gif?.let {
                GifDetailsScreenContent(
                    innerPadding = innerPadding,
                    isLoading = isLoading,
                    gif = it
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifDetailsScreenContent(
    innerPadding: PaddingValues,
    isLoading: Boolean,
    gif: Gif
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        GlideImage(
            model = gif.images.original.url,
            failure = placeholder(R.drawable.ic_placeholder),
            contentDescription = null,
            transition = CrossFade,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(WhiteSemiTransparent)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = gif.username,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = gif.title,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )

            }
        }

        if (isLoading) {
            LoadingView(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GifDetailsScreenContentPreview() {
    val gif = SampleData.getGifSample()

    GifDetailsScreenContent(
        innerPadding = PaddingValues(16.dp),
        isLoading = false,
        gif = gif
    )
}
