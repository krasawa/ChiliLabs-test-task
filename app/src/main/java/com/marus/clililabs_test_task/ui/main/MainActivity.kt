package com.marus.clililabs_test_task.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marus.clililabs_test_task.ui.details.GifDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiphyApp()
        }
    }
}

sealed class Destination(
    val route: String
) {
    data object Search : Destination("search")
    data object Details: Destination("gifDetailScreen/{gifId}") {
        fun createRoute(gifId: String) = "gifDetailScreen/$gifId"
    }
}

@Composable
fun GiphyApp() {
    val navController = rememberNavController()
    val searchViewModel = viewModel<GifSearchViewModel>()

    NavHost(navController = navController, startDestination = Destination.Search.route) {
        composable(Destination.Search.route) {
            GifSearchScreen(
                viewModel = searchViewModel,
                onNavigateToGifDetails = { gifId ->
                    navController.navigate(Destination.Details.createRoute(gifId))
                }
            )
        }
        activity(Destination.Details.route) {
            argument("gifId") { type = NavType.StringType }
            activityClass = GifDetailsActivity::class
        }
    }
}