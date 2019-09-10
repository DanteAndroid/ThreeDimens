package com.dante.threedimens.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dante.threedimens.ui.picturelist.ImageRepository
import com.dante.threedimens.ui.picturelist.PictureListViewModel

/**
 * @author Dante
 * 2019-08-23
 */
class PicturesViewModelFactory(private val imageRepository: ImageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PictureListViewModel(imageRepository) as T
    }
}