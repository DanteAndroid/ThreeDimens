package com.example.threedimens.net

import com.example.threedimens.utils.PC_USER_AGENT
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Dante
 * 2019-08-23
 */
interface BooruApi {

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("/post")
    fun getYande(@Query("page") page: Int): Call<ResponseBody>

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("index.php?page=post&s=list&tags=all")
    fun getSafe(@Query("pid") num: Int): Call<ResponseBody>

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("posts/{num}")
    fun getDan(@Path("num") num: Int): Call<ResponseBody>
}
