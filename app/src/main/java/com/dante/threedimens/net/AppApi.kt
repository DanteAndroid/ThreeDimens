package com.dante.threedimens.net

import com.dante.threedimens.data.AppInfo
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author Dante
 * 2019-08-23
 */
interface AppApi {

    @GET("app.json")
    fun getAppInfo(): Call<AppInfo>
}