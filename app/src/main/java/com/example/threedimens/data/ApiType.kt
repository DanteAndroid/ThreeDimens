package com.example.threedimens.data

import com.example.threedimens.net.API
import java.io.Serializable

/**
 * @author Du Wenyu
 * 2019-08-23
 */
data class ApiType(val site: Site, val category: String = "") : Serializable {

    var path: String = ""

    val type get() = site.name + category + path

    enum class Site(val baseUrl: String) {
        GANK(API.GANK_BASE), DOUBAN(API.DB_BASE),
        MEIZITU(API.MZ_BASE), H_FORUM(API.H_BASE)
    }

    companion object {
        val mainTypes: List<ApiType> = arrayListOf(
            ApiType(Site.GANK),
            ApiType(Site.DOUBAN, API.TYPE_DB_RANK),
            ApiType(Site.DOUBAN, API.TYPE_DB_BREAST),
            ApiType(Site.DOUBAN, API.TYPE_DB_BUTT),
            ApiType(Site.DOUBAN, API.TYPE_DB_LEG),
            ApiType(Site.DOUBAN, API.TYPE_DB_SILK)
        )

        val meizituTypes: List<ApiType> = arrayListOf(
            ApiType(Site.MEIZITU, API.TYPE_MZ_INNOCENT),
            ApiType(Site.MEIZITU, API.TYPE_MZ_JAPAN),
            ApiType(Site.MEIZITU, API.TYPE_MZ_SEXY),
            ApiType(Site.MEIZITU, API.TYPE_MZ_TAIWAN)
        )
    }
}