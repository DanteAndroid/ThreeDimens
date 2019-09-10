package com.dante.threedimens.net

import com.dante.threedimens.utils.PC_USER_AGENT
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
interface MainApi {

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("data/%E7%A6%8F%E5%88%A9/{count}/{page}")
    fun getGank(@Path("count") count: Int, @Path("page") page: Int): Call<ResponseBody>

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("/")
    fun getDouban(@Query("cid") type: String, @Query("page") page: Int): Call<ResponseBody>

}
