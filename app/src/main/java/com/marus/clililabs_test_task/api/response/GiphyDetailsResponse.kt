package com.marus.clililabs_test_task.api.response

import com.marus.clililabs_test_task.model.Gif

data class GiphyDetailsResponse(
    val data: Gif
) : BaseResponse()
