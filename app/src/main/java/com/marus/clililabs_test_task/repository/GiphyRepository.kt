package com.marus.clililabs_test_task.repository

import androidx.paging.PagingData
import com.marus.clililabs_test_task.model.Gif
import kotlinx.coroutines.flow.Flow

interface GiphyRepository {
    fun getSearchResultsStream(query: String): Flow<PagingData<Gif>>
    suspend fun findGifById(gifId: String): Gif
}