package com.example.threedimens.ui.postlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.SPUtils
import com.example.base.base.BaseStatusVM
import com.example.base.base.LoadStatus
import com.example.threedimens.data.Post
import com.example.threedimens.utils.VIEW_PAGE
import kotlinx.coroutines.launch

/**
 * 维护每个 tab 下的页面及其数据
 */
class PostListViewModel(private val repository: PostRepository) : BaseStatusVM() {

    private var page = SPUtils.getInstance().getInt(VIEW_PAGE + repository.getType(), 1)

    val posts: LiveData<List<Post>> = repository.getPosts()

    fun refreshPosts() {
        viewModelScope.launch {
            fetchPosts()
        }
    }

    fun loadMoreImages() {
        viewModelScope.launch {
            fetchPosts(++page)
            println("LoadPage ${repository.getType()} $page ")
        }
    }


    @Synchronized
    private suspend fun fetchPosts(pageNum: Int = 1) {
        try {
            setStatus(LoadStatus.LOADING)
            val result = repository.fetchPosts(pageNum)
            println("insert ${result.size} ${result.first()}")
            repository.insert(result)
            setStatus(LoadStatus.DONE)

        } catch (e: Exception) {
            e.printStackTrace()
            setStatus(LoadStatus.ERROR)
            page--
        }
    }

    override fun onCleared() {
        super.onCleared()
        SPUtils.getInstance().put(VIEW_PAGE + repository.getType(), page)
    }
}