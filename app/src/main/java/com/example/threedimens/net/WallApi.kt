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
    companion object {
        const val WALL_HAVEN_RATIOS = "9x16,1x1,10x16,3x2,9x18,4x3,5x4"
        const val AT_LEAST_RESOLUTION = "800x1000"
    }

    @Headers("User-Agent: $PC_USER_AGENT")
    @GET("search")
    fun getWalls(
        @Query("q") type: String,
        @Query("ratios") ratios: String,
        @Query("atleast") atLeast: String,
        @Query("page") page: Int
    ): Call<ResponseBody>

}
