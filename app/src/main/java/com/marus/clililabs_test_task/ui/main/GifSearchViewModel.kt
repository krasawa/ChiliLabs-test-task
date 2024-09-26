package com.marus.clililabs_test_task.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marus.clililabs_test_task.model.Gif
import com.marus.clililabs_test_task.repository.GiphyRepository
import com.marus.clililabs_test_task.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifSearchViewModel @Inject constructor(
    private val repository: GiphyRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    companion object {
        private const val TAG = "GiphyViewModel"
    }

    private var currentQueryValue: String? = null

    private val _currentSearchResult: MutableStateFlow<PagingData<Gif>> = MutableStateFlow(value = PagingData.empty())
    val currentSearchResult: MutableStateFlow<PagingData<Gif>> get() = _currentSearchResult

    val isNetworkAvailable = networkMonitor.isConnected

    fun searchGifs(query: String) {
        Log.d(TAG, "searchGifs() called with: query = $query")
        if (query.isNotEmpty() && query != currentQueryValue) {
            currentQueryValue = query

            viewModelScope.launch {
                repository
                    .getSearchResultsStream(query)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        Log.d(TAG, "Result received")
                        _currentSearchResult.value = pagingData
                    }
            }
        }
    }

    fun clearSearchResults() {
        _currentSearchResult.value = PagingData.empty()
    }
}