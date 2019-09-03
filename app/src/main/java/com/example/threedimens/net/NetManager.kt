package com.example.threedimens.net

import com.example.base.net.NetService
import com.example.threedimens.main.ApiType

/**
 * @author Du Wenyu
 * 2019-08-23
 */
object NetManager {

    fun getApiFor(apiType: ApiType): Any {
        return when (apiType.site) {
            ApiType.Site.GANK -> gankApi
            ApiType.Site.DOUBAN -> dbApi
            ApiType.Site.MEIZITU -> postApi
            ApiType.Site.YAKEXI -> postApi
            else -> throw IllegalStateException("${apiType.type} not implemented in ${javaClass.canonicalName}")
        }
    }

    val gankApi: GankApi by lazy {
        NetService.getInstance().createApi<GankApi>(API.GANK_BASE)
    }
    val dbApi: DBApi by lazy {
        NetService.getInstance().createApi<DBApi>(API.DB_BASE)
    }
    val postApi: PostApi by lazy {
        NetService.getInstance().createApi<PostApi>(API.MZ_BASE)
    }
    val wallApi: WallApi by lazy {
        NetService.getInstance().createApi<WallApi>(API.WALL_BASE)
    }
    val yakexiApi: ForumApi by lazy {
        NetService.getInstance().createApi<ForumApi>(API.YAKEXI_BASE)
    }
    val shtApi: ForumApi by lazy {
        NetService.getInstance().createApi<ForumApi>(API.SHT_BASE)
    }


}