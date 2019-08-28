package com.example.threedimens.ui.picturelist

import androidx.lifecycle.LiveData
import com.example.threedimens.data.ApiType
import com.example.threedimens.data.Image
import com.example.threedimens.data.ImageDao
import com.example.threedimens.net.DataParser.getImages
import com.example.threedimens.net.NetManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class ImageRepository(private val apiType: ApiType, private val imageDao: ImageDao) {

    fun getImages(): LiveData<List<Image>> {
        return imageDao.getImages(apiType.type)
    }

    suspend fun fetchImages(page: Int): List<Image> {
        return withContext(IO) {
            when (apiType.site) {
                ApiType.Site.GANK -> {
                    getImages(apiType, NetManager.gankApi.get(20, page))
                }
                ApiType.Site.DOUBAN -> {
                    getImages(apiType, NetManager.dbApi.get(apiType.path, page))
                }
                ApiType.Site.MEIZITU -> {
                    getImages(apiType, NetManager.postApi.getPictures(apiType.path, page))
                }
                ApiType.Site.H_FORUM -> {
                    getImages(apiType, NetManager.postApi.getPictures(apiType.path, page))
                }
            }
        }
    }


    suspend fun insert(image: Image) {
        withContext(IO) {
            imageDao.insert(image)
        }
    }

    suspend fun insert(images: List<Image>) {
        withContext(IO) {
            imageDao.insert(images)
        }
    }

    suspend fun delete(image: Image) {
        withContext(IO) {
            imageDao.delete(image)
        }
    }

}