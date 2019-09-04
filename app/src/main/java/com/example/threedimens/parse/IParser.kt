package com.example.threedimens.parse

import com.example.threedimens.data.Image
import com.example.threedimens.data.Post
import com.example.threedimens.main.ApiType

/**
 * @author Du Wenyu
 * 2019-09-03
 */
interface IParser {

    fun parsePosts(apiType: ApiType, data: String): List<Post>
    fun parseImages(apiType: ApiType, data: String): List<Image>

}