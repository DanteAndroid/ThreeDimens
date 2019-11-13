package com.dante.threedimens.data.parse

import com.dante.threedimens.data.Image
import com.dante.threedimens.data.Post
import com.dante.threedimens.ui.main.ApiType
import org.jsoup.Jsoup
import java.io.IOException

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
        val document = Jsoup.parse(data)
        val elements = document.select("div[id=thumbs] figure")
        for (element in elements) {
            try {
                val img = element.selectFirst("img")
                val url = img.attr("data-src")
                if (url.isBlank()) continue
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
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return images
    }

    private fun getOriginalUrl(thumbUrl: String): String {
        val id = thumbUrl.substringAfterLast("/").substringBefore(".")
        val tail = thumbUrl.substringAfterLast("small").replace(id, "wallhaven-$id")
        return "https://w.wallhaven.cc/full$tail"
    }

}