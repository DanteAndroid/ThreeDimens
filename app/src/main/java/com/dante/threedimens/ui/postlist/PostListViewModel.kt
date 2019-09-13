package com.dante.threedimens.ui.postlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.SPUtils
import com.dante.base.base.BaseStatusVM
import com.dante.base.base.LoadStatus
import com.dante.threedimens.data.Post
import com.dante.threedimens.utils.VIEW_PAGE
import com.dante.threedimens.utils.VIEW_POSITION
import kotlinx.coroutines.launch

/**
 * 维护每个 tab 下的页面及其数据
 */
class PostListViewModel(private val repository: PostRepository) : BaseStatusVM() {

    private var page = SPUtils.getInstance().getInt(VIEW_PAGE + repository.getType(), 1)

    val posts: LiveData<List<Post>> = repository.getPosts()

    fun refreshPosts() {
        viewModelScope.launch {
            repository.deleteAll()
            fetchPosts()
        }
    }

    fun loadMorePosts() {
        viewModelScope.launch {
            fetchPosts(++page)
        }
    }

    private suspend fun fetchPosts(pageNum: Int = 1) {
        try {
            setStatus(LoadStatus.LOADING)
            val result = repository.fetchPosts(pageNum)
            repository.insert(result)
            setStatus(LoadStatus.DONE)

        } catch (e: Exception) {
            e.printStackTrace()
            setStatus(LoadStatus.ERROR)
            page--
        }
    }

    fun saveLastPosition(lastPosition: Int) {
        if (lastPosition > 0) {
            SPUtils.getInstance().put(VIEW_POSITION + repository.getType(), lastPosition)
        }
    }

    fun getLastPosition(): Int {
        return SPUtils.getInstance().getInt(VIEW_POSITION + repository.getType(), 0)
    }

    override fun onCleared() {
        super.onCleared()
        SPUtils.getInstance().put(VIEW_PAGE + repository.getType(), page)
    }


}