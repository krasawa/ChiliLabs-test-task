package com.marus.clililabs_test_task.data.model

data class Gif(
    val id: String,
    val title: String,
    val images: Images,
    val username: String
)

data class Images(
    val original: OriginalImage
)

data class OriginalImage(
    val url: String
)