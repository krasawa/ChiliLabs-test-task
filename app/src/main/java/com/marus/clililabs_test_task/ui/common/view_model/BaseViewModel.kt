package com.marus.clililabs_test_task.ui.common.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel: ViewModel() {
    abstract val TAG: String

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    protected fun setLoading(value: Boolean) {
        Log.d(TAG, "setLoadingState: $value")
        _isLoading.value = value
    }
}