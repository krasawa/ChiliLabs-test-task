package com.marus.clililabs_test_task.ui.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GifDetailsActivity : ComponentActivity() {

    companion object {
        const val GIF_ID_EXTRA = "gifId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val gifDetailsViewModel = viewModel<GifDetailsViewModel>()

            GiftDetailsScreen(
                gifId = intent?.getStringExtra(GIF_ID_EXTRA)!!,
                viewModel = gifDetailsViewModel
            )
        }
    }
}

