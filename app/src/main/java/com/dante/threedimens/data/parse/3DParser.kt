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
object `3DParser` : IParser {

    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        throw IllegalStateException("${javaClass.simpleName} has no posts")
    }

    override fun parseImages(apiType: ApiType, data: String): List<Image> {
        val images = arrayListOf<Image>()
        val document = Jsoup.parse(data)
        val elements = document.select("div[id=post-list] span[class=thumb blacklisted]")

        for (element in elements) {
            try {
                val refer = API.`3DBOORU_BASE` + element.selectFirst("a").attr("href")
                val thumbUrl = element.selectFirst("img").attr("src")
                if (thumbUrl.isNullOrBlank()) continue
                val url = getOriginalUrl(thumbUrl)
                images.add(
                    Image(
                        id = thumbUrl,
                        url = thumbUrl,
                        originalUrl = url,
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

    private fun getOriginalUrl(thumbUrl: String) =
        thumbUrl.replace("sample/", "")
            .replace("preview/", "")


}