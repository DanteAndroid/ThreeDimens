package com.dante.threedimens.net

import com.dante.base.net.NetService

/**
 * @author Dante
 * 2019-08-23
 */
object NetManager {

//    fun getApiFor(apiType: ApiType): Any {
//        return when (apiType.site) {
//            ApiType.Site.GANK -> gankApi
//            ApiType.Site.DOUBAN -> dbApi
//            ApiType.Site.MEIZITU -> meiziApi
//            ApiType.Site.YAKEXI -> meiziApi
//            else -> throw IllegalStateException("${apiType.type} not implemented in ${javaClass.canonicalName}")
//        }
//    }

    val appApi: AppApi by lazy {
        NetService.getInstance().createApi<AppApi>(API.GITHUB_RAW)
    }
    val gankApi: MainApi by lazy {
        NetService.getInstance().createApi<MainApi>(API.GANK_BASE)
    }
    val dbApi: MainApi by lazy {
        NetService.getInstance().createApi<MainApi>(API.DB_BASE)
    }

    val meiziApi: MeiziApi by lazy {
        NetService.getInstance().createApi<MeiziApi>(API.MZ_BASE)
    }
    val meituluApi: MeiziApi by lazy {
        NetService.getInstance().createApi<MeiziApi>(API.MEITULU_BASE)
    }
    val wallApi: WallApi by lazy {
        NetService.getInstance().createApi<WallApi>(API.WALL_BASE)
    }
    val yandeApi: BooruApi by lazy {
        NetService.getInstance().createApi<BooruApi>(API.YANDE_BASE)
    }
    val safeApi: BooruApi by lazy {
        NetService.getInstance().createApi<BooruApi>(API.SAFEBOORU_BASE)
    }
    val danApi: BooruApi by lazy {
        NetService.getInstance().createApi<BooruApi>(API.DANBOORU_BASE)
    }
    val `3dApi`: BooruApi by lazy {
        NetService.getInstance().createApi<BooruApi>(API.`3DBOORU_BASE`)
    }

    val yakexiApi: ForumApi by lazy {
        NetService.getInstance().createApi<ForumApi>(API.YAKEXI_BASE)
    }
    val shtApi: ForumApi by lazy {
        NetService.getInstance().createApi<ForumApi>(API.SHT_BASE)
    }

}