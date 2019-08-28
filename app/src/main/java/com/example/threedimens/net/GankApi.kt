package com.example.threedimens.net

import com.example.threedimens.utils.PC_USER_AGENT
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by yons on 16/12/8.
 */

interface GankApi {

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("data/%E7%A6%8F%E5%88%A9/{count}/{page}")
    fun get(@Path("count") count: Int, @Path("page") page: Int): Call<ResponseBody>

    class Result<T> {
        var error: Boolean = false
        var results: T? = null
    }
}
