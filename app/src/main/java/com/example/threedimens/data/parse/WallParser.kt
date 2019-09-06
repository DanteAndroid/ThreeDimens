package com.example.threedimens.data.parse

import androidx.annotation.WorkerThread
import com.example.threedimens.data.Image
import com.example.threedimens.data.Post
import com.example.threedimens.ui.main.ApiType
import com.example.threedimens.utils.AppUtil
import org.jsoup.Jsoup
import java.io.IOException
import javax.net.ssl.SSLContext

/**
 * @author Dante
 * 2019-09-03
 */
object WallParser : IParser {

    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        throw IllegalStateException("${javaClass.simpleName} has no posts")
    }

    override fun parseImages(apiType: ApiType, data: String): List<Image> {
        val images = arrayListOf<Image>()
        try {
            val document = Jsoup.parse(data)
            val elements = document.select("div[id=thumbs] figure")
            for (element in elements) {
                val img = element.selectFirst("img")
                val url = img.attr("data-src")
                val originalUrl = getOriginalUrl(url)
                val refer = element.selectFirst("a").attr("href")
                images.add(
                    Image(
                        id = url,
                        url = url,
                        originalUrl = originalUrl,
                        type = apiType.type,
                        post = refer
                    )
                )
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return images
    }

    private fun getOriginalUrl(thumbUrl: String): String {
        val id = thumbUrl.substringAfterLast("/").substringBefore(".")
        val tail = thumbUrl.substringAfterLast("small").replace(id, "wallhaven-$id")
        return "https://w.wallhaven.cc/full$tail"
    }

    @WorkerThread
    fun parseOriginalUrl(refer: String): String {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(AppUtil.createUnsafeTrustManager()), null)
        return Jsoup.connect(refer).sslSocketFactory(sslContext.socketFactory).get()
            .selectFirst("main[id=main] img").attr("src")
    }

}