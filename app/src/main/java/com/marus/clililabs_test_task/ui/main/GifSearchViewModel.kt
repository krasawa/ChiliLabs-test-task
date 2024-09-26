package com.marus.clililabs_test_task.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marus.clililabs_test_task.model.Gif
import com.marus.clililabs_test_task.repository.GiphyRepository
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
    private val repository: GiphyRepository
) : ViewModel() {

    companion object {
        private const val TAG = "GiphyViewModel"
    }

    private var currentQueryValue: String? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentSearchResult: MutableStateFlow<PagingData<Gif>> = MutableStateFlow(value = PagingData.empty())
    val currentSearchResult: MutableStateFlow<PagingData<Gif>> get() = _currentSearchResult

    fun searchGifs(query: String) {
        Log.d(TAG, "searchGifs() called with: query = $query")
        if (query != currentQueryValue) {
            setLoading(true)
            currentQueryValue = query

            viewModelScope.launch {
                repository
                    .getSearchResultsStream(query)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        Log.d(TAG, "Result received")
                        _currentSearchResult.value = pagingData

                        delay(500)
                        setLoading(false)
                    }
            }

        } else {
            Log.d(TAG, "the same query, returning last result")
        }
    }

    private fun setLoading(value: Boolean) {
        Log.d(TAG, "setLoadingState: $value")
        _isLoading.value = value
    }
}