package com.example.threedimens.data.parse

import androidx.annotation.WorkerThread
import com.example.threedimens.data.Image
import com.example.threedimens.data.Post
import com.example.threedimens.ui.main.ApiType
import org.jsoup.Jsoup
import java.io.IOException

/**
 * @author Du Wenyu
 * 2019-09-05
 */
object YandeParser : IParser {
    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parseImages(apiType: ApiType, data: String): List<Image> {
        val images = arrayListOf<Image>()
        try {
            val document = Jsoup.parse(data)
            val elements = document.select("ul[id=post-list-posts] li")
            for (element in elements) {
                val a = element.selectFirst("a[class=thumb]")
                val original = element.selectFirst("a[class=directlink largeimg]")
                val url = original.attr("href")
                val refer = a.attr("href")
                val thumbUrl = a.selectFirst("img").attr("src")
                println("parse Images ${elements.size}, $refer $url")
                images.add(Image(id = url, url = thumbUrl, type = apiType.type, post = refer))
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return images
    }

    @WorkerThread
    fun getOriginalUrl(thumbUrl: String): String {
        return ""
    }
}