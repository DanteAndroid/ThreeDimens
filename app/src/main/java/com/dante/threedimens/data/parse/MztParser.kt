package com.dante.threedimens.data.parse

import android.text.TextUtils
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
object MztParser : IParser {

    override fun parsePosts(apiType: ApiType, data: String): List<Post> {
        val posts = arrayListOf<Post>()

        val document = Jsoup.parse(data)
        val elements = document.select("div[class=postlist] li")
        for (element in elements) {
            try {
                val aElement = element.selectFirst("a")
                val link = aElement.attr("href")
                val img = aElement.selectFirst("img")
                val title = img.attr("alt")
                val src = img.attr("data-original")
                posts.add(Post(postUrl = link, type = apiType.type, coverUrl = src, title = title))
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
            val aElements = document.select("div[class=pagenavi] a")
            var totalPage = 1
            if (aElements != null && aElements.size > 3) {
                val pageStr = aElements.get(aElements.size - 2).text()
                if (!TextUtils.isEmpty(pageStr) && TextUtils.isDigitsOnly(pageStr)) {
                    totalPage = Integer.parseInt(pageStr)
                }
            }
            val src = document.getElementsByClass("main-image").first()
                .selectFirst("img").attr("src")
            for (index in 1 until totalPage + 1) {
                val url: String = if (index < 10) {
                    src.replace("01.", "0$index.")
                } else {
                    src.replace("01.", "$index.")
                }
                val refer = API.MZ_BASE + apiType.path
//            image.setTotalPage(1)//无需loadmore
                images.add(Image(id = url, url = url, type = apiType.type, post = refer))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return images
    }

}