package com.example.threedimens.data.parse

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
        throw IllegalStateException("${javaClass.simpleName} has no posts")
    }

    override fun parseImages(apiType: ApiType, data: String): List<Image> {
        val images = arrayListOf<Image>()
        val document = Jsoup.parse(data)
        val elements = document.select("ul[id=post-list-posts] li")
        for (element in elements) {
            try {
                val a = element.selectFirst("a[class=thumb]")
                val original = element.selectFirst("a[class=directlink largeimg]")
                val url = original.attr("href")
                val refer = a.attr("href")
                val thumbUrl = a.selectFirst("img").attr("src")
                images.add(
                    Image(
                        id = url,
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