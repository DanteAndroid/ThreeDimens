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
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * 维护每个 tab 下的页面及其数据
 */
class PostListViewModel(private val repository: PostRepository) : BaseStatusVM() {

    private var page = SPUtils.getInstance().getInt(VIEW_PAGE + repository.getType(), 1)

    val posts: LiveData<List<Post>> = repository.getPosts()

    fun refreshPosts(deleteOld: Boolean = false) {
        viewModelScope.launch {
            if (deleteOld) {
                repository.deleteAll()
            }
            fetchPosts()
        }
    }

    fun loadMorePosts() {
        viewModelScope.launch {
            if (fetchPosts(page + 1)) {
                page++
            }
        }
    }

    private suspend fun fetchPosts(pageNum: Int = 1): Boolean {
        try {
            setStatus(LoadStatus.LOADING)
            val result = repository.fetchPosts(pageNum)
            repository.insert(result)
            setStatus(LoadStatus.SUCCESS)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is SocketException, is SocketTimeoutException -> {
                    setStatus(LoadStatus.NET_ERROR)
                }
                else -> {
                    setStatus(LoadStatus.FAIL)
                }
            }
        }
        return false
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