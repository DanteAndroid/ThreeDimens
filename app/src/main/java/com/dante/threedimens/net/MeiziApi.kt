package com.dante.threedimens.net

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Dante
 * 2019-08-23
 */
interface MeiziApi {

    @GET("{type}/page/{page}/")
    fun getPosts(@Path("type") type: String, @Path("page") page: Int): Call<ResponseBody>

    @GET("/{post}/{page}")
    fun getPictures(@Path("post") postUrl: String, @Path("page") page: Int): Call<ResponseBody>

    @GET("/{type}/{page}")
    fun getMeituluPosts(@Path("type") type: String, @Path("page") page: String): Call<ResponseBody>

    @GET("/{post}")
    fun getMeituluPictures(@Path("post") postUrl: String): Call<ResponseBody>
}
