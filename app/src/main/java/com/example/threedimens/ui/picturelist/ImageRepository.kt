package com.example.threedimens.ui.picturelist

import androidx.paging.DataSource
import com.example.threedimens.data.ApiType
import com.example.threedimens.data.DataParser.getPostImages
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
class ImageRepository(val apiType: ApiType, private val imageDao: ImageDao) {

    fun getType(): String = apiType.type

    fun getPagedImages(): DataSource.Factory<Int, Image> {
        println("get images ${apiType.type} ${apiType.path}")
        return imageDao.getPagedImages(apiType.type)
    }

    suspend fun fetchImages(page: Int): List<Image> {
        return withContext(IO) {
            when (apiType.site) {
                ApiType.Site.GANK -> {
                    getPostImages(apiType, NetManager.gankApi.get(PAGE_SIZE_FROM_NET, page))
                }
                ApiType.Site.DOUBAN -> {
                    getPostImages(apiType, NetManager.dbApi.get(apiType.category, page))
                }
                ApiType.Site.MEIZITU -> {
                    getPostImages(apiType, NetManager.postApi.getPictures(apiType.path, page))
                }
                ApiType.Site.H_FORUM -> {
                    getPostImages(apiType, NetManager.postApi.getPictures(apiType.path, page))
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


}