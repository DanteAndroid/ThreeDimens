package com.example.base.net

import com.example.base.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author Du Wenyu
 * 2019-08-20
 */
class NetService private constructor(val url: String = "") {

    companion object {
        @Volatile
        private var instance: NetService? = null

        fun getInstance(url: String = "") =
            instance ?: synchronized(this) {
                instance ?: NetService(url).also { instance = it }
            }
    }

    var netApi: Any? = null

    inline fun <reified T> getApi(): T {
        if (netApi == null) {
            synchronized(this::class.java) {
                if (netApi == null) {
                    netApi = createRetrofit(url).create(T::class.java)
                }
            }
        }
        return netApi!! as T
    }

    inline fun <reified T> createApi(baseUrl: String = url, okHttpClient: OkHttpClient? = null): T {
        if (okHttpClient == null) return createRetrofit(baseUrl).create(T::class.java)
        return createRetrofit(baseUrl, okHttpClient).create(T::class.java)
    }

    fun createRetrofit(baseUrl: String, okHttpClient: OkHttpClient = createOkHttpClient()): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
//            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    fun getBasicOkHttpClient(): OkHttpClient.Builder {
        val loggingInterceptor = HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return getBasicOkHttpClient().build()
    }

}