package com.dante.threedimens.data.parse

import com.dante.threedimens.data.Image
import com.dante.threedimens.data.Post
import com.dante.threedimens.net.API
import com.dante.threedimens.ui.main.ApiType
import org.jsoup.Jsoup
import java.io.IOException

/**
 * @author Du Wenyu
 * 2019-09-05
 */
object SafeParser : IParser {

    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        throw IllegalStateException("${javaClass.simpleName} has no posts")
    }

    override fun parseImages(apiType: ApiType, data: String): List<Image> {
        val images = arrayListOf<Image>()
        val document = Jsoup.parse(data)
        val elements = document.select("div[class=content] a")
        for (element in elements) {
            try {
                val refer = API.SAFEBOORU_BASE + element.attr("href")
                val a = element.selectFirst("img")
                val thumbUrl = a?.selectFirst("img")?.attr("src")
                val url = getOriginalUrl(thumbUrl)
                thumbUrl?.let {
                    images.add(
                        Image(
                            id = thumbUrl,
                            url = thumbUrl,
                            originalUrl = url,
                            type = apiType.type,
                            post = refer
                        )
                    )
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return images
    }

    private fun getOriginalUrl(thumbUrl: String?): String {
        if (thumbUrl == null) return ""
        return thumbUrl.replaceFirst("thumbnails", "images").replace("thumbnail_", "")
    }
}