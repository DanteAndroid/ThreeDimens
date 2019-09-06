package com.example.threedimens.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.base.base.BaseStatusVM
import com.example.threedimens.data.Image
import com.example.threedimens.data.parse.WallParser
import com.example.threedimens.ui.main.ApiType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Dante
 * 2019-08-25
 */
class PictureViwerViewModel(private val repository: PictureViewerRepository) : BaseStatusVM() {

    private val _position: MutableLiveData<Int> = MutableLiveData()
    val position: LiveData<Int> = _position

    fun getImages() = repository.getImages()

    fun getImage(id: String) = repository.getImage(id)


    fun setPosition(position: Int) {
        _position.value = position
    }


    fun fetchRealUrl(image: Image) {
        println("fetchRealUrl $image")
        if (!image.type.contains(ApiType.Site.WALLHAVEN.name)) return
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val originalUrl = WallParser.parseOriginalUrl(image.post)
                    val realImage = image.copy(url = originalUrl)
                    repository.update(realImage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}