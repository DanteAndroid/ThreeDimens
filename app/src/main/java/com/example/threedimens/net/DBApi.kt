package com.example.threedimens.net

import com.example.threedimens.utils.PC_USER_AGENT
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by yons on 16/12/8.
 */
interface DBApi {

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("index")
    fun get(@Query("cid") type: String, @Query("pager_offset") page: Int): Call<ResponseBody>

    @GET("rank.htm")
    fun getRank(@Query("pager_offset") page: Int): Call<ResponseBody>


}
