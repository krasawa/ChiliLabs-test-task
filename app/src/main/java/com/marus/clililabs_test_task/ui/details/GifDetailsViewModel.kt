package com.marus.clililabs_test_task.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marus.clililabs_test_task.model.Gif
import com.marus.clililabs_test_task.repository.GiphyRepository
import com.marus.clililabs_test_task.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifDetailsViewModel @Inject constructor(
    private val repository: GiphyRepository
) : ViewModel() {

    companion object {
        private const val TAG = "GifDetailsViewModel"
    }

    private val _gif = MutableStateFlow<Gif?>(null)
    val gif: StateFlow<Gif?> = _gif.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun findGifById(id: String) {
        if (_gif.value == null) {
            Log.d(TAG, "findGifById() called with: id = $id")
            viewModelScope.launch {
                try {
                    setLoading(true)

                    val gif = repository.findGifById(id)
                    _gif.value = gif
                } catch (e: Exception) {
                    Log.e(TAG, "Error searching GIFs", e)
                    e.message?.let {
                        showError(it)
                    }
                } finally {
                    delay(500)
                    setLoading(false)
                }
            }
        } else {
            Log.d(TAG, "gif already loaded")
        }
    }

    private fun setLoading(value: Boolean) {
        _isLoading.value = value
    }

    private fun showError(message: String) {
        _errorMessage.value = message
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }
}