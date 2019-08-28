package com.example.threedimens.net

import com.example.threedimens.data.AppInfo
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author Du Wenyu
 * 2019-08-23
 */
interface AppApi {

    @GET("app.json")
    fun getAppInfo(): Call<AppInfo>
}