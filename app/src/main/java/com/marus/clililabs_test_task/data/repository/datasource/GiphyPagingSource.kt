package com.marus.clililabs_test_task.data.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marus.clililabs_test_task.BuildConfig
import com.marus.clililabs_test_task.data.api.service.GiphyApi
import com.marus.clililabs_test_task.data.model.Gif

class GiphyPagingSource(
    private val api: GiphyApi,
    private val query: String
) : PagingSource<Int, Gif>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        val position = params.key ?: 0
        return try {
            val response = api.searchGifs(
                apiKey = BuildConfig.GIPHY_API_KEY,
                query = query,
                limit = params.loadSize,
                offset = position
            )

            LoadResult.Page(
                data = response.data,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = if (response.data.isEmpty()) null else position + params.loadSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
