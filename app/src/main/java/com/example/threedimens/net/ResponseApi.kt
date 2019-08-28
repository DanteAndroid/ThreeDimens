package com.example.threedimens.net

import okhttp3.ResponseBody
import retrofit2.Call

/**
 * @author Du Wenyu
 * 2019-08-23
 */
interface ResponseApi {

    fun getPictures(type: String, page: Int): Call<ResponseBody>

}