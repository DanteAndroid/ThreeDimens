package com.dante.threedimens.ui.picturelist

import androidx.paging.DataSource
import com.dante.threedimens.data.Image
import com.dante.threedimens.data.ImageDao
import com.dante.threedimens.data.parse.DataParser.getImages
import com.dante.threedimens.net.API
import com.dante.threedimens.net.NetManager
import com.dante.threedimens.net.WallApi
import com.dante.threedimens.net.WallApi.Companion.AT_LEAST_RESOLUTION
import com.dante.threedimens.net.WallApi.Companion.BETTER_RESOLUTION
import com.dante.threedimens.net.WallApi.Companion.WALL_HAVEN_PORTRAIT_RATIOS
import com.dante.threedimens.net.WallApi.Companion.WALL_HAVEN_RATIOS
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.utils.PAGE_SIZE_FROM_NET
import com.dante.threedimens.utils.PAGE_SIZE_FROM_NET_LARGE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @author Dante
 * 2019-08-23
 */
class ImageRepository(val apiType: ApiType, private val imageDao: ImageDao) {

    fun getType(): String = apiType.type

    fun getPagedImages(): DataSource.Factory<Int, Image> {
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
                    val isRandom = apiType.category == API.CATE_WH_RANDOM

                    getImages(
                        apiType,
                        NetManager.wallApi.getWalls(
                            type = if (isRandom) "" else apiType.category,
                            ratios = if (isRandom) WALL_HAVEN_PORTRAIT_RATIOS else WALL_HAVEN_RATIOS,
                            atLeast = if (isRandom) BETTER_RESOLUTION else AT_LEAST_RESOLUTION,
                            page = if (isRandom) null else page,
                            sort = if (isRandom) WallApi.SORT_RANDOM else WallApi.SORT_RELEVANCE
                        )
                    )
                }
                ApiType.Site.MEIZITU -> {
                    getImages(apiType, NetManager.meiziApi.getPictures(apiType.path, page))
                }
                ApiType.Site.YANDE -> {
                    getImages(apiType, NetManager.yandeApi.getYande(page))
                }
                ApiType.Site.SAFEBOORU -> {
                    getImages(apiType, NetManager.safeApi.getSafe(page * PAGE_SIZE_FROM_NET_LARGE))
                }
                ApiType.Site.DANBOORU -> {
                    getImages(apiType, NetManager.danApi.getDan(page, apiType.category))
                }
                ApiType.Site.MTL -> {
                    getImages(apiType, NetManager.meituluApi.getMeituluPictures(apiType.path))
                }
                ApiType.Site.`3DBOORU` -> {
                    getImages(apiType, NetManager.`3dApi`.get3D(page))
                }
//                ApiType.Site.YAKEXI -> {
//                    getImages(apiType, NetManager.meiziApi.getPictures(apiType.path, page))
//                }
                ApiType.Site.SEHUATANG -> {
                    getImages(apiType, NetManager.shtApi.getPictures(apiType.path))
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


    suspend fun deleteAll() {
        withContext(IO) {
            println("deleteAll ${apiType.type}")
            imageDao.deleteAll(apiType.type)
        }
    }
}