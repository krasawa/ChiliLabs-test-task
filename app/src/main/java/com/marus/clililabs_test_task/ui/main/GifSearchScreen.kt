package com.marus.clililabs_test_task.ui.main

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.marus.clililabs_test_task.R
import com.marus.clililabs_test_task.model.Gif
import com.marus.clililabs_test_task.ui.common.LoadingView
import com.marus.clililabs_test_task.ui.theme.CliliLabstesttaskTheme
import com.marus.clililabs_test_task.ui.theme.ScreenBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GifSearchScreen(
    onNavigateToGifDetails: (gifId: String) -> Unit,
    viewModel: GifSearchViewModel
) {
    var query by rememberSaveable { mutableStateOf("") }

    val gifs = viewModel.currentSearchResult.collectAsLazyPagingItems()
    val isConnected by viewModel.isNetworkAvailable.collectAsState()

    var loadingFirstPage by rememberSaveable { mutableStateOf(false) }
    var loadingNextPage by rememberSaveable { mutableStateOf(false) }
    val isEmptyViewVisible = rememberSaveable(query) {
        query.isEmpty()
    }
    var isNoResultsViewVisible by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    CliliLabstesttaskTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(ScreenBackground)
            ) {
                Column {
                    SearchBar(
                        query = query,
                        onQueryChanged = { newQuery ->
                            query = newQuery
                            if (newQuery.isEmpty()) {
                                viewModel.clearSearchResults()
                            }
                        },
                        onSearch = {
                            viewModel.searchGifs(query)
                        }
                    )

                    val columns = remember(configuration.orientation) {
                        if (configuration.orientation == ORIENTATION_PORTRAIT) {
                            2
                        } else {
                            3
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(gifs.itemCount) { index ->
                            val gif = gifs[index]
                            if (gif != null) {
                                GifItem(
                                    gif = gif,
                                    onClick = {
                                        if (isConnected) {
                                            onNavigateToGifDetails(gif.id)
                                        } else {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = context.getString(R.string.error_no_internet)
                                                )
                                            }
                                        }
                                    })
                            }
                        }
                        if (loadingNextPage) {
                            item(span = {
                                GridItemSpan(maxLineSpan)
                            }) {
                                LoadingView(modifier = Modifier)
                            }
                        }
                    }

                    LaunchedEffect(gifs.loadState) {
                        when (gifs.loadState.refresh) {
                            is LoadState.Loading -> {
                                Log.d("SearchScreen", "refresh: Loading")
                                loadingFirstPage = true
                            }

                            is LoadState.NotLoading -> {
                                Log.d("SearchScreen", "refresh: NotLoading")
                                loadingFirstPage = false
                                isNoResultsViewVisible =
                                    gifs.loadState.append.endOfPaginationReached && gifs.itemCount == 0
                            }

                            is LoadState.Error -> {
                                Log.d("SearchScreen", "refresh: Error")
                                loadingFirstPage = false
                            }
                        }

                        loadingNextPage = when (gifs.loadState.append) {
                            is LoadState.Loading -> {
                                Log.d("SearchScreen", "append: Loading")
                                true
                            }

                            is LoadState.NotLoading -> {
                                Log.d("SearchScreen", "append: NotLoading")
                                false
                            }

                            is LoadState.Error -> {
                                Log.d("SearchScreen", "append: Error")
                                false
                            }
                        }
                    }
                }
                if (loadingFirstPage) {
                    LoadingView(
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    if (isEmptyViewVisible) {
                        EmptyView(
                            modifier = Modifier.align(Alignment.Center),
                            stringResId = R.string.empty_view_text_no_query
                        )
                    } else if (isNoResultsViewVisible) {
                        EmptyView(
                            modifier = Modifier.align(Alignment.Center),
                            stringResId = R.string.empty_view_text_no_results
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    val debouncePeriod = 500L
    var searchJob by remember { mutableStateOf<Job?>(null) }

    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                onQueryChanged("")
            },
        ) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "",
                tint = Color.Black
            )
        }
    }

    TextField(
        value = query,
        onValueChange = { newQuery ->
            onQueryChanged(newQuery)
            searchJob?.cancel()
            searchJob = CoroutineScope(Dispatchers.Main).launch {
                delay(debouncePeriod)
                onSearch()
            }
        },
        label = {
            Text(
                text = stringResource(R.string.search_hint)
            )
        },
        shape = RoundedCornerShape(12.dp),
        trailingIcon = if (query.isNotBlank()) trailingIconView else null,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        singleLine = true,

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifItem(
    gif: Gif,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            model = gif.images.original.url,
            loading = placeholder(R.drawable.ic_loading),
            failure = placeholder(R.drawable.ic_placeholder),
            contentDescription = null,
            transition = CrossFade,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Composable
fun EmptyView(
    modifier: Modifier,
    stringResId: Int
) {
    Text(
        text = stringResource(stringResId),
        fontSize = 22.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun EmptyViewPreview() {
    EmptyView(Modifier, R.string.empty_view_text_no_query)
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(
        query = "Funny cats",
        onQueryChanged = {},
        onSearch = {}
    )
}
