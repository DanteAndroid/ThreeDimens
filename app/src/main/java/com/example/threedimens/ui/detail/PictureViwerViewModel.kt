package com.example.threedimens.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.base.base.BaseStatusVM

/**
 * @author Du Wenyu
 * 2019-08-25
 */
class PictureViwerViewModel(private val repository: PictureViewerRepository) : BaseStatusVM() {

    private val _position: MutableLiveData<Int> = MutableLiveData()
    val position: LiveData<Int> = _position

    fun getImages() = repository.getImages()


    fun setPosition(position: Int) {
        _position.value = position
    }

}