package com.marus.clililabs_test_task.ui.util

import com.marus.clililabs_test_task.model.Gif
import com.marus.clililabs_test_task.model.Images
import com.marus.clililabs_test_task.model.OriginalImage

object SampleData {
    fun getGifSample(id: String = "12345"): Gif {
        return Gif(
            id = id,
            title = "Funny Cat",
            images = Images(
                original = OriginalImage(
                    url = "https://media.giphy.com/media/abc123/original.gif"
                )
            ),
            username = "catlover"
        )
    }
}