package com.dante.threedimens.data.parse

import com.dante.threedimens.data.Image
import com.dante.threedimens.data.Post
import com.dante.threedimens.net.API
import com.dante.threedimens.ui.main.ApiType
import org.jsoup.Jsoup
import java.io.IOException

/**
 * @author Dante
 * 2019-09-03
 */
object ShtParser : IParser {

    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        val posts = arrayListOf<Post>()

        val document = Jsoup.parse(data)
        val elements = document.select("th[class=new]")
//        val infos = document.select("td[class=by]")
        for (element in elements) {
            try {
                val aElement = element.select("a[class=s xst]")
                val link = API.SHT_BASE + aElement.attr("href")
                val title = aElement.text()
                posts.add(Post(postUrl = link, type = apiType.type, title = title))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return posts
    }


    override fun parseImages(apiType: ApiType, data: String): List<Image> {
        val images = arrayListOf<Image>()
        try {
            val document = Jsoup.parse(data)
            val elements = document.select("td[class=t_f] img")

            for (i in 0 until elements.size) {
                val src = elements[i].attr("file")
                println("sht ${elements[i].textNodes().size}")
                if (src.isBlank()) continue
                images.add(Image(id = src, type = apiType.type, url = src))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return images
    }

}