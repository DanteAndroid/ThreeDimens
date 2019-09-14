package com.dante.threedimens.ui.main

import android.os.Parcel
import android.os.Parcelable
import com.dante.threedimens.data.parse.*
import com.dante.threedimens.net.API

/**
 * @author Dante
 * 2019-08-23
 */
class ApiType(val site: Site, val category: String = "", var path: String = "") : Parcelable {
    val type get() = site.name + category + path

    enum class Site(val baseUrl: String, val parser: IParser, val spanCount: Int = 2) {
        GANK(API.GANK_BASE, GankParser), DOUBAN(API.DB_BASE, DoubanParser, 3),
        MEIZITU(API.MZ_BASE, MztParser), YAKEXI(API.YAKEXI_BASE, MztParser),
        SEHUATANG(API.SHT_BASE, ShtParser), WALLHAVEN(API.WALL_BASE, WallParser),
        YANDE(API.YANDE_BASE, YandeParser), SAFEBOORU(API.SAFEBOORU_BASE, SafeParser, 3),
        DANBOORU(API.DANBOORU_BASE, DanParser, 3), `3DBOORU`(API.`3DBOORU_BASE`, `3DParser`, 3),
        MTL(API.MEITULU_BASE, MtlParser)
    }

    constructor(source: Parcel) : this(
        Site.values()[source.readInt()],
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(site.ordinal)
        writeString(category)
        writeString(path)
    }

    companion object {
        val menuGank: Array<ApiType> = arrayOf(
            ApiType(Site.GANK),
            ApiType(Site.DOUBAN, API.CATE_DB_RANK),
            ApiType(Site.DOUBAN, API.CATE_DB_BREAST),
            ApiType(Site.DOUBAN, API.CATE_DB_BUTT),
            ApiType(Site.DOUBAN, API.CATE_DB_LEG),
            ApiType(Site.DOUBAN, API.CATE_DB_SILK)
        )

        val menuMeizitu: Array<ApiType> = arrayOf(
            ApiType(Site.MEIZITU, API.CATE_MZ_HOT),
            ApiType(Site.MEIZITU, API.CATE_MZ_INNOCENT),
            ApiType(Site.MEIZITU, API.CATE_MZ_SEXY),
            ApiType(Site.MTL, API.CATE_MTL_CHINESE),
            ApiType(Site.MTL, API.CATE_MTL_MODEL),
            ApiType(Site.MTL, API.CATE_MTL_BREAST),
            ApiType(Site.MTL, API.CATE_MTL_COS)
        )

        val menuWallHaven: Array<ApiType> = arrayOf(
            ApiType(Site.WALLHAVEN, API.CATE_WH_RANDOM),
            ApiType(Site.WALLHAVEN, API.CATE_WH_ANIME),
            ApiType(Site.WALLHAVEN, API.CATE_WH_FANTASY),
            ApiType(Site.WALLHAVEN, API.CATE_WH_GIRL),
            ApiType(Site.WALLHAVEN, API.CATE_WH_LANDSCAPE),
            ApiType(Site.WALLHAVEN, API.CATE_WH_DARK),
            ApiType(Site.WALLHAVEN, API.CATE_WH_SIMPLE)
        )

        val menuBooru: Array<ApiType> = arrayOf(
            ApiType(Site.SAFEBOORU),
            ApiType(Site.YANDE),
            ApiType(Site.DANBOORU),
            ApiType(Site.DANBOORU, API.CATE_DAN_HOT),
            ApiType(Site.`3DBOORU`)
        )

        val menuSehuatang: Array<ApiType> = arrayOf(
            ApiType(Site.SEHUATANG, API.CATE_SHT_CHINESE),
            ApiType(Site.SEHUATANG, API.CATE_SHT_STREET),
            ApiType(Site.SEHUATANG, API.CATE_SHT_ASIA),
            ApiType(Site.SEHUATANG, API.CATE_SHT_US),
            ApiType(Site.SEHUATANG, API.CATE_SHT_CARTON),
            ApiType(Site.SEHUATANG, API.CATE_SHT_DISCUSS)
        )

        @JvmField
        val CREATOR: Parcelable.Creator<ApiType> = object : Parcelable.Creator<ApiType> {
            override fun createFromParcel(source: Parcel): ApiType = ApiType(source)
            override fun newArray(size: Int): Array<ApiType?> = arrayOfNulls(size)
        }
    }
}