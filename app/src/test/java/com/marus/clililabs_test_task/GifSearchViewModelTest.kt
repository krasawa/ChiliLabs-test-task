package com.marus.clililabs_test_task

import androidx.paging.PagingData
import com.marus.clililabs_test_task.ui.main.GifSearchViewModel
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GifSearchViewModelTest {
    @get:Rule
//    val coroutineRule = MainCoroutineRule()

//    private val repository = mockk<GiphyRepository>()
    private lateinit var viewModel: GifSearchViewModel

    @Before
    fun setup() {
//        viewModel = GifSearchViewModel(repository)
    }

//    @Test
//    fun `searchGifs returns paging data`() = runTest {
//        val pagingData = PagingData.from(listOf(Gif(...)))
//        coEvery { repository.getSearchResultsStream(any()) } returns flowOf(pagingData)
//
//        val result = viewModel.searchGifs("test").first()
//
//        assertNotNull(result)
//    }
}