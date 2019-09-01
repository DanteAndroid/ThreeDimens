package com.example.threedimens.ui.postlist

import androidx.lifecycle.LiveData
import com.example.threedimens.data.ApiType
import com.example.threedimens.data.DataParser.getPosts
import com.example.threedimens.data.Image
import com.example.threedimens.data.Post
import com.example.threedimens.data.PostDao
import com.example.threedimens.net.NetManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @author Du Wenyu
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
                    getPosts(apiType, NetManager.postApi.getPosts(apiType.category, page))
                }
                ApiType.Site.H_FORUM -> {
                    getPosts(apiType, NetManager.postApi.getPosts(apiType.category, page))
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