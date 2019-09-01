package com.example.threedimens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.threedimens.ui.picturelist.ImageRepository
import com.example.threedimens.ui.picturelist.PictureListViewModel

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class PicturesViewModelFactory(private val imageRepository: ImageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PictureListViewModel(imageRepository) as T
    }
}