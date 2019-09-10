package com.dante.threedimens.data.parse

import com.dante.threedimens.data.Image
import com.dante.threedimens.data.Post
import com.dante.threedimens.ui.main.ApiType

/**
 * @author Dante
 * 2019-09-03
 */
interface IParser {

    fun parsePosts(apiType: ApiType, data: String): List<Post>

    fun parseImages(apiType: ApiType, data: String): List<Image>

}