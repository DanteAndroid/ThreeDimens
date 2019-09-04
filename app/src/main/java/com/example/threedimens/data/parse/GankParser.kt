package com.example.threedimens.data.parse

import com.example.threedimens.data.Image
import com.example.threedimens.data.Post
import com.example.threedimens.ui.main.ApiType
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import java.util.*

/**
 * @author Du Wenyu
 * 2019-09-03
 */
object GankParser : IParser {

    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        throw IllegalStateException("${javaClass.simpleName} has no posts")
    }

    override fun parseImages(apiType: ApiType, data: String): List<Image> {
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
}