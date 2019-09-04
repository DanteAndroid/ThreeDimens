package com.example.threedimens.data.parse

import com.example.threedimens.data.Image
import com.example.threedimens.data.Post
import com.example.threedimens.ui.main.ApiType
import okhttp3.ResponseBody
import retrofit2.Call


/**
 * @author Du Wenyu
 * 2019-08-23
 */
object DataParser {

    fun getImages(apiType: ApiType, call: Call<ResponseBody>): List<Image> {
        try {
            return apiType.site.parser.parseImages(apiType, call.execute().body()!!.string())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    fun getPosts(apiType: ApiType, call: Call<ResponseBody>): List<Post> {
        try {
            return apiType.site.parser.parsePosts(apiType, call.execute().body()!!.string())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

}