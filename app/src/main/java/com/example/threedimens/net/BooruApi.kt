package com.example.threedimens.net

import com.example.threedimens.utils.PC_USER_AGENT
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * @author Dante
 * 2019-08-23
 */
interface BooruApi {

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("/post")
    fun get(@Query("page") page: Int): Call<ResponseBody>

}
