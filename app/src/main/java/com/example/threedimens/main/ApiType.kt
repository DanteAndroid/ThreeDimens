package com.example.threedimens.main

import android.os.Parcel
import android.os.Parcelable
import com.example.threedimens.net.API
import com.example.threedimens.parse.*

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class ApiType(val site: Site, val category: String = "") : Parcelable {
    var path: String = ""

    val type get() = site.name + category + path

    enum class Site(val baseUrl: String, val parser: IParser) {
        GANK(API.GANK_BASE, GankParser), DOUBAN(API.DB_BASE, DbParser),
        MEIZITU(API.MZ_BASE, MztParser), YAKEXI(API.YAKEXI_BASE, MztParser),
        SEHUATANG(API.SHT_BASE, MztParser), WALLHAVEN(API.WALL_BASE, WallParser)
    }

    constructor(source: Parcel) : this(
        Site.values()[source.readInt()],
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(site.ordinal)
        writeString(category)
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
            ApiType(Site.MEIZITU, API.CATE_MZ_INNOCENT),
            ApiType(Site.MEIZITU, API.CATE_MZ_JAPAN),
            ApiType(Site.MEIZITU, API.CATE_MZ_SEXY),
            ApiType(Site.MEIZITU, API.CATE_MZ_TAIWAN)
        )

        val menuWallHaven: Array<ApiType> = arrayOf(
            ApiType(Site.WALLHAVEN, API.CATE_WH_GIRL),
            ApiType(Site.WALLHAVEN, API.CATE_WH_ANIME),
            ApiType(Site.WALLHAVEN, API.CATE_WH_FANTASY),
            ApiType(Site.WALLHAVEN, API.CATE_WH_LANDSCAPE)
        )

        @JvmField
        val CREATOR: Parcelable.Creator<ApiType> = object : Parcelable.Creator<ApiType> {
            override fun createFromParcel(source: Parcel): ApiType =
                ApiType(source)

            override fun newArray(size: Int): Array<ApiType?> = arrayOfNulls(size)
        }
    }
}