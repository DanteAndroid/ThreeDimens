package com.example.threedimens.ui.detail

import androidx.lifecycle.LiveData
import com.example.threedimens.data.Image
import com.example.threedimens.data.ImageDao

/**
 * @author Dante
 * 2019-08-25
 */
class PictureViewerRepository(private val type: String, private val imageDao: ImageDao) {

    fun getImages(): LiveData<List<Image>> {
        return imageDao.getImages(type)
    }


}