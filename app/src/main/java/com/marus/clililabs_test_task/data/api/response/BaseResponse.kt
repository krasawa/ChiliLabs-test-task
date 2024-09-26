package com.marus.clililabs_test_task.data.api.response

abstract class BaseResponse {
    private val meta: ResponseMeta? = null
    fun isSuccessful() = meta?.status == 200
    fun getErrorMessage() = meta?.msg
}

data class ResponseMeta(
    val status: Int,
    val msg: String
)