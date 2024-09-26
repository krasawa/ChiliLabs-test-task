package com.marus.clililabs_test_task

import com.marus.clililabs_test_task.data.repository.GiphyRepository
import com.marus.clililabs_test_task.ui.details.GifDetailsViewModel
import com.marus.clililabs_test_task.ui.util.SampleData
import com.marus.clililabs_test_task.util.Logger
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GifDetailsViewModelTest {
    @Mock
    private lateinit var repository: GiphyRepository
    @Mock
    private lateinit var logger: Logger
    private lateinit var viewModel: GifDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GifDetailsViewModel(repository, logger)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findGifById successfully loads gif`() = runTest {
        val gifId = "test_id"
        val mockGif = SampleData.getGifSample(id = gifId)

        whenever(repository.findGifById(gifId)).thenReturn(mockGif)

        viewModel.findGifById(gifId)

        advanceTimeBy(100)
        assertEquals(true, viewModel.isLoading.first())

        assertEquals(mockGif, viewModel.gif.first())
        advanceTimeBy(500)

        assertEquals(false, viewModel.isLoading.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findGifById handles error correctly`() = runTest {
        val gifId = "invalid_id"
        val errorMessage = "Gif not found"

        whenever(repository.findGifById(gifId)).thenThrow(RuntimeException(errorMessage))

        viewModel.findGifById(gifId)

        advanceTimeBy(100)
        assertEquals(errorMessage, viewModel.errorMessage.first())

        advanceTimeBy(500)
        assertEquals(false, viewModel.isLoading.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findGifById doesn't fetch gif if already loaded`() = runTest {
        val gifId = "test_id"
        val mockGif = SampleData.getGifSample(id = gifId)

        whenever(repository.findGifById(gifId)).thenReturn(mockGif)

        viewModel.findGifById(gifId)
        advanceTimeBy(500)
        viewModel.findGifById(gifId)

        verify(repository, times(1)).findGifById(gifId)

        assertEquals(mockGif, viewModel.gif.first())
    }
}