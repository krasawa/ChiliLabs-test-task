package com.marus.clililabs_test_task.data.api.service

import com.marus.clililabs_test_task.BuildConfig
import com.marus.clililabs_test_task.data.api.response.GiphyDetailsResponse
import com.marus.clililabs_test_task.data.api.response.GiphySearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GiphyApi {
    companion object {
        private const val API_KEY = BuildConfig.GIPHY_API_KEY
    }

    @GET("v1/gifs/search")
    suspend fun searchGifs(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GiphySearchResponse

    @GET("v1/gifs/{gif_id}")
    suspend fun findGifById(
        @Path("gif_id") gifId: String,
        @Query("api_key") apiKey: String = API_KEY
    ): GiphyDetailsResponse
}