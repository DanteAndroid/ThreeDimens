package com.dante.threedimens.net

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Dante
 * 2019-08-23
 */
interface ForumApi {

    //Post示例
    @GET("forum-{type}-{page}.html")
    fun getPosts(@Path("type") type: String, @Path("page") page: Int): Call<ResponseBody>

    @GET("/{post}")
    fun getPictures(@Path("post") postUrl: String): Call<ResponseBody>

}
