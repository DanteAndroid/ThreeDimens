package com.dante.threedimens.ui.postlist

import androidx.lifecycle.LiveData
import com.dante.threedimens.data.Image
import com.dante.threedimens.data.Post
import com.dante.threedimens.data.PostDao
import com.dante.threedimens.data.parse.DataParser.getPosts
import com.dante.threedimens.net.NetManager
import com.dante.threedimens.ui.main.ApiType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @author Dante
 * 2019-08-23
 */
class PostRepository(private val apiType: ApiType, private val postDao: PostDao) {

    fun getType(): String = apiType.type

    fun getPostImages(post: Post): LiveData<List<Image>> {
        return postDao.getImagesOfPost(post.postUrl)
    }

    fun getPosts(): LiveData<List<Post>> {
        return postDao.getPosts(apiType.type)
    }

    suspend fun fetchPosts(page: Int): List<Post> {
        return withContext(IO) {
            when (apiType.site) {
                ApiType.Site.MEIZITU -> {
                    getPosts(apiType, NetManager.MEIZI_API.getPosts(apiType.category, page))
                }
                ApiType.Site.YAKEXI -> {
                    getPosts(apiType, NetManager.MEIZI_API.getPosts(apiType.category, page))
                }
                else -> throw IllegalStateException("Not implemented: $apiType")
            }
        }
    }

    suspend fun insert(post: Post) {
        withContext(IO) {
            postDao.insert(post)
        }
    }

    suspend fun insert(list: List<Post>) {
        withContext(IO) {
            postDao.insert(list)
        }
    }

}