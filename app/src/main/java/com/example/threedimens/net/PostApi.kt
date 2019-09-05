package com.example.threedimens.net

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Dante
 * 2019-08-23
 */
interface PostApi {

    //Post示例
    @GET("{type}/page/{page}/")
    fun getPosts(@Path("type") type: String, @Path("page") page: Int): Call<ResponseBody>

    @GET("/{post}/{page}")
    fun getPictures(@Path("post") postUrl: String, @Path("page") page: Int): Call<ResponseBody>

}
