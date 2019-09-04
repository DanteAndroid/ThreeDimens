package com.example.threedimens.data.parse

import com.example.threedimens.data.Image
import com.example.threedimens.data.Post
import com.example.threedimens.ui.main.ApiType
import org.jsoup.Jsoup
import java.io.IOException

/**
 * @author Du Wenyu
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
            println("${javaClass.canonicalName} elements ${elements.size}")
            for (element in elements) {
                val img = element.selectFirst("img")
                val thumbUrl = img.attr("data-src")
                val url = getOriginalUrl(thumbUrl)
                val refer = element.selectFirst("a").attr("href")
//                val originUrl = parseOriginalUrl(refer)
                images.add(Image(id = url, url = url, type = apiType.type, post = refer))
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

    fun parseOriginalUrl(refer: String): String {
        return Jsoup.connect(refer).get().selectFirst("main[id=main] img").attr("src")
    }

}