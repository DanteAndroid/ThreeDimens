package com.example.base.base

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel

/**
 * @author Du Wenyu
 * 2019-08-23
 */
enum class LoadStatus { LOADING, ERROR, DONE }

abstract class BaseStatusVM : ViewModel() {

    private val _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus> get() = _status

    @MainThread
    fun setStatus(loadStatus: LoadStatus) {
        _status.value = loadStatus
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}