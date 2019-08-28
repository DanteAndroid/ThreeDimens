package com.example.threedimens.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import org.jsoup.Jsoup
import retrofit2.Call
import java.io.IOException
import java.util.*


/**
 * @author Du Wenyu
 * 2019-08-23
 */
object DataParser {

    fun getPagedImages(apiType: ApiType, call: Call<ResponseBody>): List<Image> {
        return parseList(
            apiType,
            call.execute().body()?.string()
        )
    }

    /**
     * 解析一级页面的所有图片
     */
    fun parseList(apiType: ApiType, data: String?): List<Image> {
        if (data == null) return emptyList()
        return when (apiType.site) {
            ApiType.Site.GANK -> {
                parseGank(data, apiType)
            }
            ApiType.Site.DOUBAN -> {
                parseDB(data, apiType)
            }
            ApiType.Site.MEIZITU -> {
                parseMzt(data, apiType)
            }
            ApiType.Site.H_FORUM -> {
                parseH(data, apiType)
            }
        }
    }

    private class ImageAdapter(val apiType: ApiType) {
        private val moshi = Moshi.Builder().build()

        @ToJson
        fun toJson(image: Image): String? {
            return moshi.adapter(Image::class.java).toJson(image)
        }

        @FromJson
        fun fromJson(data: String): Image? {
            return moshi.adapter(Image::class.java).fromJson(data)?.apply {
                type = apiType.type
                println("parseGank $type")
            }
        }
    }

    private fun parseGank(data: String, apiType: ApiType): List<Image> {
        val array = JSONObject(data).getString("results")
        val types = Types.newParameterizedType(List::class.java, Image::class.java)
        val images = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
//            .add(ImageAdapter(apiType))
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter<List<Image>>(types)
            .lenient()
            .fromJson(array)?.map {
                it.type = apiType.type
                it
            }
        return images!!
    }

    private fun parseH(data: String, apiType: ApiType): List<Image> {
        val posts = arrayListOf<Image>()
        return posts
    }

    private fun parseMzt(data: String, apiType: ApiType): List<Image> {
        val posts = arrayListOf<Image>()
        //TODO
        return posts
    }

    private fun parseDB(data: String, apiType: ApiType): List<Image> {
        val images = arrayListOf<Image>()
        try {
            val document = Jsoup.parse(data)
            //  Elements elements = document.select("div[class=thumbnail] > div[class=img_single] > a > img");
            val elements = document.select("div[class=thumbnail] div[class=img_single] img")
            for (i in 0 until elements.size) {
                val src = elements[i].attr("src").trim()
                images.add(Image(type = apiType.type, url = src))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return images
    }
}