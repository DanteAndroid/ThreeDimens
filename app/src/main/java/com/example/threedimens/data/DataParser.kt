package com.example.threedimens.data

import android.text.TextUtils
import com.example.threedimens.net.API
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

    fun getPostImages(
        apiType: ApiType,
        call: Call<ResponseBody>
    ): List<Image> {
        return parseList(
            apiType,
            call.execute().body()?.string()
        )
    }

    fun getPosts(apiType: ApiType, call: Call<ResponseBody>): List<Post> {
        return parsePost(apiType, call.execute().body()?.string())
    }

    private fun parsePost(apiType: ApiType, data: String?): List<Post> {
        if (data == null) return emptyList()
        return when (apiType.site) {
            ApiType.Site.MEIZITU -> {
                parseMztPosts(data, apiType)
            }
            ApiType.Site.H_FORUM -> {
                parseHPosts(data, apiType)
            }
            else -> emptyList()
        }
    }

    private fun parseMztPosts(data: String, apiType: ApiType): List<Post> {
        val posts = arrayListOf<Post>()
        try {
            val document = Jsoup.parse(data)
            val elements = document.select("div[class=postlist] li")
            for (element in elements) {
                val aElement = element.selectFirst("a")
                val link = aElement.attr("href")
                val img = aElement.selectFirst("img")
                val title = img.attr("alt")
                val src = img.attr("data-original")
                posts.add(Post(postUrl = link, type = apiType.type, coverUrl = src, title = title))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return posts
    }

    private fun parseHPosts(data: String, apiType: ApiType): List<Post> {
        println("parseHposts ")
        return emptyList()
    }

    /**
     * 解析图片列表（一般是原图）
     */
    private fun parseList(apiType: ApiType, data: String?): List<Image> {
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
        val images = arrayListOf<Image>()
        val document = Jsoup.parse(data)
        val aElements = document.select("div[class=pagenavi] a")
        var totalPage = 1
        if (aElements != null && aElements.size > 3) {
            val pageStr = aElements.get(aElements.size - 2).text()
            if (!TextUtils.isEmpty(pageStr) && TextUtils.isDigitsOnly(pageStr)) {
                totalPage = Integer.parseInt(pageStr)
                println("parseMzt total page is $totalPage")
            }
        }
        val src = document.getElementsByClass("main-image").first().selectFirst("img").attr("src")
        for (index in 1 until totalPage + 1) {
            val url: String = if (index < 10) {
                src.replace("01.", "0$index.")
            } else {
                src.replace("01.", "$index.")
            }
            val refer = API.MZ_BASE + apiType.path
//            image.setTotalPage(1)//无需loadmore
            images.add(Image(url = url, type = apiType.type, post = refer))
        }
        return images
    }

    private fun parseDB(data: String, apiType: ApiType): List<Image> {
        val images = arrayListOf<Image>()
        try {
            val document = Jsoup.parse(data)
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