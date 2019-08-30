package com.example.threedimens.ui.picturelist

import androidx.paging.DataSource
import com.example.threedimens.data.ApiType
import com.example.threedimens.data.DataParser.getPagedImages
import com.example.threedimens.data.Image
import com.example.threedimens.data.ImageDao
import com.example.threedimens.net.NetManager
import com.example.threedimens.utils.PAGE_SIZE_FROM_NET
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class ImageRepository(private val apiType: ApiType, private val imageDao: ImageDao) {

    fun getPagedImages(): DataSource.Factory<Int, Image> {
        return imageDao.getPagedImages(apiType.type)
    }

    suspend fun fetchImages(page: Int): List<Image> {
        return withContext(IO) {
            when (apiType.site) {
                ApiType.Site.GANK -> {
                    getPagedImages(apiType, NetManager.gankApi.get(PAGE_SIZE_FROM_NET, page))
                }
                ApiType.Site.DOUBAN -> {
                    getPagedImages(apiType, NetManager.dbApi.get(apiType.path, page))
                }
                ApiType.Site.MEIZITU -> {
                    getPagedImages(apiType, NetManager.postApi.getPictures(apiType.path, page))
                }
                ApiType.Site.H_FORUM -> {
                    getPagedImages(apiType, NetManager.postApi.getPictures(apiType.path, page))
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