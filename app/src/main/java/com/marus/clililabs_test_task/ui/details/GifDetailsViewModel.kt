package com.marus.clililabs_test_task.ui.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.marus.clililabs_test_task.model.Gif
import com.marus.clililabs_test_task.repository.GiphyRepository
import com.marus.clililabs_test_task.ui.common.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifDetailsViewModel @Inject constructor(
    private val repository: GiphyRepository
) : BaseViewModel() {

    override val TAG = "GifDetailsViewModel"

    private val _gif = MutableStateFlow<Gif?>(null)
    val gif: StateFlow<Gif?> = _gif.asStateFlow()

    fun findGifById(id: String) {
        Log.d(TAG, "findGifById() called with: id = $id")
        viewModelScope.launch {
            try {
                setLoading(true)

                val gif = repository.findGifById(id)
                _gif.value = gif
            } catch (e: Exception) {
                Log.e(TAG, "Error searching GIFs", e)
            } finally {
                setLoading(false)
            }
        }
    }
}