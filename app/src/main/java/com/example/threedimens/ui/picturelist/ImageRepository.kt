package com.example.threedimens.ui.picturelist

import androidx.paging.DataSource
import com.example.threedimens.data.Image
import com.example.threedimens.data.ImageDao
import com.example.threedimens.data.parse.DataParser.getImages
import com.example.threedimens.net.NetManager
import com.example.threedimens.net.WallApi.Companion.AT_LEAST_RESOLUTION
import com.example.threedimens.net.WallApi.Companion.WALL_HAVEN_RATIOS
import com.example.threedimens.ui.main.ApiType
import com.example.threedimens.utils.PAGE_SIZE_FROM_NET
import com.example.threedimens.utils.PAGE_SIZE_FROM_NET_LARGE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @author Dante
 * 2019-08-23
 */
class ImageRepository(val apiType: ApiType, private val imageDao: ImageDao) {

    fun getType(): String = apiType.type

    fun getPagedImages(): DataSource.Factory<Int, Image> {
        println("getType ${apiType.type}")
        return imageDao.getPagedImages(apiType.type)
    }

    suspend fun fetchImages(page: Int): List<Image> {
        return withContext(IO) {
            when (apiType.site) {
                ApiType.Site.GANK -> {
                    getImages(apiType, NetManager.gankApi.getGank(PAGE_SIZE_FROM_NET, page))
                }
                ApiType.Site.DOUBAN -> {
                    getImages(apiType, NetManager.dbApi.getDouban(apiType.category, page))
                }
                ApiType.Site.WALLHAVEN -> {
                    getImages(
                        apiType,
                        NetManager.wallApi.getWalls(
                            apiType.category,
                            WALL_HAVEN_RATIOS,
                            AT_LEAST_RESOLUTION,
                            page
                        )
                    )
                }
                ApiType.Site.MEIZITU -> {
                    getImages(apiType, NetManager.MEIZI_API.getPictures(apiType.path, page))
                }
                ApiType.Site.YANDE -> {
                    getImages(apiType, NetManager.yandeApi.getYande(page))
                }
                ApiType.Site.SAFEBOORU -> {
                    getImages(apiType, NetManager.safeApi.getSafe(page * PAGE_SIZE_FROM_NET_LARGE))
                }
                ApiType.Site.DANBOORU -> {
                    getImages(apiType, NetManager.danApi.getDan(page))
                }
                ApiType.Site.YAKEXI -> {
                    getImages(apiType, NetManager.MEIZI_API.getPictures(apiType.path, page))
                }
                else -> throw IllegalStateException("${apiType.type} not implemented in ${javaClass.canonicalName}")
            }
        }
    }


    suspend fun insert(images: List<Image>) {
        withContext(IO) {
            imageDao.insert(images)
        }
    }


}