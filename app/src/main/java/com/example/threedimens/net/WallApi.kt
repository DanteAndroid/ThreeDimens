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
interface WallApi {

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("search")
    fun getWalls(@Query("q") type: String, @Query("page") page: Int): Call<ResponseBody>

}