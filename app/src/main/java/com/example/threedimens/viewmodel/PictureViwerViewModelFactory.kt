package com.example.threedimens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.threedimens.ui.detail.PictureViewerRepository
import com.example.threedimens.ui.detail.PictureViwerViewModel

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class PictureViwerViewModelFactory(private val repository: PictureViewerRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PictureViwerViewModel(repository) as T
    }
}