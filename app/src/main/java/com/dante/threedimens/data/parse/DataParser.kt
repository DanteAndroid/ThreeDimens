package com.dante.threedimens.data.parse

import com.dante.threedimens.data.Image
import com.dante.threedimens.data.Post
import com.dante.threedimens.ui.main.ApiType
import okhttp3.ResponseBody
import retrofit2.Call


/**
 * @author Dante
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