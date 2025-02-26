package com.marus.clililabs_test_task.data.api.response

import com.google.gson.annotations.SerializedName
import com.marus.clililabs_test_task.data.model.Gif

data class GiphySearchResponse(
    val data: List<Gif>,
    val pagination: Pagination
) : BaseResponse()

data class Pagination(
    @SerializedName("total_count") val totalCount: Int,
    val count: Int,
    val offset: Int
)