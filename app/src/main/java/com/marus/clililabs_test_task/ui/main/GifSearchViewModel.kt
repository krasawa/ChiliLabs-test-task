package com.marus.clililabs_test_task.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marus.clililabs_test_task.data.model.Gif
import com.marus.clililabs_test_task.data.repository.GiphyRepository
import com.marus.clililabs_test_task.util.Logger
import com.marus.clililabs_test_task.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifSearchViewModel @Inject constructor(
    private val repository: GiphyRepository,
    private val networkMonitor: NetworkMonitor,
    private val logger: Logger
) : ViewModel() {

    companion object {
        private const val TAG = "GifSearchViewModel"
    }

    private var currentQueryValue: String? = null

    private val _currentSearchResult: MutableStateFlow<PagingData<Gif>> = MutableStateFlow(value = PagingData.empty())
    val currentSearchResult: MutableStateFlow<PagingData<Gif>> get() = _currentSearchResult

    val isNetworkAvailable = networkMonitor.isConnected

    fun searchGifs(query: String) {
        logger.d(TAG, "searchGifs() called with: query = $query")
        if (query.isNotEmpty() && query != currentQueryValue) {
            currentQueryValue = query

            viewModelScope.launch {
                repository
                    .getSearchResultsStream(query)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        logger.d(TAG, "Result received")
                        _currentSearchResult.value = pagingData
                    }
            }
        }
    }

    fun clearSearchResults() {
        _currentSearchResult.value = PagingData.empty()
        currentQueryValue = null
    }
}