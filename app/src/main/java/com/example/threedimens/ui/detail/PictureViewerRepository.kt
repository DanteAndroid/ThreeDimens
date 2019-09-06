package com.example.threedimens.ui.detail

import androidx.lifecycle.LiveData
import com.example.threedimens.data.Image
import com.example.threedimens.data.ImageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Dante
 * 2019-08-25
 */
class PictureViewerRepository(private val type: String, private val imageDao: ImageDao) {

    fun getImages(): LiveData<List<Image>> {
        return imageDao.getImages(type)
    }

    fun getImage(id: String): LiveData<Image> {
        return imageDao.getImage(id)
    }

    suspend fun update(image: Image) {
        withContext(Dispatchers.IO) {
            imageDao.update(image)
        }
    }
}