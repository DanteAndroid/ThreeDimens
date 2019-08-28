package com.example.threedimens.ui.detail

import androidx.lifecycle.LiveData
import com.example.threedimens.data.Image
import com.example.threedimens.data.ImageDao

/**
 * @author Du Wenyu
 * 2019-08-25
 */
class PictureViwerRepository(private val type: String, private val imageDao: ImageDao) {

    fun getImages(): LiveData<List<Image>> {
        println("getImages ${imageDao.getImages(type).value?.size}")
        return imageDao.getImages(type)
    }
}