package com.dante.threedimens.ui.picturelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.blankj.utilcode.util.SPUtils
import com.dante.base.base.BaseStatusVM
import com.dante.base.base.LoadStatus
import com.dante.threedimens.data.Image
import com.dante.threedimens.ui.main.ApiType.Companion.noLoadMoreSite
import com.dante.threedimens.utils.PAGE_SIZE_FROM_DB
import com.dante.threedimens.utils.VIEW_PAGE
import com.dante.threedimens.utils.VIEW_POSITION
import kotlinx.coroutines.launch
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * 维护每个 tab 下的页面及其数据
 */
class PictureListViewModel(private val repository: ImageRepository) : BaseStatusVM() {

    private var page = SPUtils.getInstance().getInt(VIEW_PAGE + repository.getType(), 1)

    val pagedImages: LiveData<PagedList<Image>> = repository.getPagedImages()
        .toLiveData(pageSize = PAGE_SIZE_FROM_DB, boundaryCallback = ImageBoundaryCallback(this))

    fun refreshImages(deleteOld: Boolean = false) {
        viewModelScope.launch {
            if (deleteOld) {
                repository.deleteAll()
            }
            fetchImages()
        }
    }

    /**
     * 加载下一页的图片
     * @return 无需加载则返回false
     */
    fun loadMoreImages(): Boolean {
        if (repository.apiType.site in noLoadMoreSite) {
            // 不需要加载更多页因为已经解析完所有图片了
            return false
        }
        viewModelScope.launch {
            if (fetchImages(page + 1)) {
                page++
            }
        }
        return true
    }

    /**
     * 加载某页图片列表
     * @return 加载成功则返回true
     */
    private suspend fun fetchImages(pageNum: Int = 1): Boolean {
        try {
            setStatus(LoadStatus.LOADING)
            val result = repository.fetchImages(pageNum)
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