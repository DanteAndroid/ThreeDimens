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
object DoubanParser : IParser {

    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        throw IllegalStateException("${javaClass.simpleName} has no posts")
    }

    override fun parseImages(apiType: ApiType, data: String): List<Image> {
        val images = arrayListOf<Image>()
        try {
            val document = Jsoup.parse(data)
            val elements = document.select("div[class=thumbnail] div[class=img_single] img")
            for (i in 0 until elements.size) {
                val src = elements[i].attr("src").trim()
                if (src.isBlank()) continue
                images.add(Image(id = src, type = apiType.type, url = src))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return images
    }


}