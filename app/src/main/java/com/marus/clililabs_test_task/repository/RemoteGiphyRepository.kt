package com.marus.clililabs_test_task.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marus.clililabs_test_task.api.service.GiphyApi
import com.marus.clililabs_test_task.model.Gif
import com.marus.clililabs_test_task.repository.datasource.GiphyPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteGiphyRepository @Inject constructor(
    private val api: GiphyApi
): GiphyRepository {

    override fun getSearchResultsStream(query: String): Flow<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GiphyPagingSource(api, query) }
        ).flow
    }

    override suspend fun findGifById(gifId: String): Gif {
        val response = api.findGifById(gifId = gifId)
        if (response.isSuccessful()) {
            return response.data
        } else {
            throw Exception(response.getErrorMessage())
        }
    }
}