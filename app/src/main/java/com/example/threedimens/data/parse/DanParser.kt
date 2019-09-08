package com.example.threedimens.data.parse

import com.example.threedimens.data.Image
import com.example.threedimens.data.Post
import com.example.threedimens.net.API
import com.example.threedimens.ui.main.ApiType
import org.jsoup.Jsoup
import java.io.IOException

/**
 * @author Du Wenyu
 * 2019-09-05
 */
object DanParser : IParser {

    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        throw IllegalStateException("${javaClass.simpleName} has no posts")
    }

    override fun parseImages(apiType: ApiType, data: String): List<Image> {
        val images = arrayListOf<Image>()
        val document = Jsoup.parse(data)
        val elements = document.select("div[id=posts] article")
        for (element in elements) {
            try {
                val refer = API.DANBOORU_BASE + element.selectFirst("a").attr("href")
                val thumbUrl = element.attr("data-preview-file-url")
                val url = element.attr("data-file-url")
                println("${javaClass.name} ${elements.size}, $refer $thumbUrl, $url")
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


}