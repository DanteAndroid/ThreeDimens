package com.example.threedimens.ui.picturelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.blankj.utilcode.util.SPUtils
import com.example.base.base.BaseStatusVM
import com.example.base.base.LoadStatus
import com.example.threedimens.data.Image
import com.example.threedimens.data.parse.WallParser
import com.example.threedimens.ui.main.ApiType
import com.example.threedimens.utils.PAGE_SIZE_FROM_DB
import com.example.threedimens.utils.VIEW_PAGE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 维护每个 tab 下的页面及其数据
 */
class PictureListViewModel(private val repository: ImageRepository) : BaseStatusVM() {

    private var page = SPUtils.getInstance().getInt(VIEW_PAGE + repository.getType(), 1)

    val pagedImages: LiveData<PagedList<Image>> = repository.getPagedImages()
        .toLiveData(pageSize = PAGE_SIZE_FROM_DB, boundaryCallback = ImageBoundaryCallback(this))

    fun refreshImages() {
        viewModelScope.launch {
            fetchImages()
        }
    }

    fun loadMoreImages() {
        viewModelScope.launch {
            fetchImages(++page)
            println("LoadPage ${repository.getType()} $page ")
        }
    }


    @Synchronized
    private suspend fun fetchImages(pageNum: Int = 1) {
        try {
            setStatus(LoadStatus.LOADING)
            val result = repository.fetchImages(pageNum)
            repository.update(result)
            setStatus(LoadStatus.DONE)
        } catch (e: Exception) {
            e.printStackTrace()
            setStatus(LoadStatus.ERROR)
            page--
        }
    }

    fun fetchRealUrl(image: Image) {
        if (!image.type.contains(ApiType.Site.WALLHAVEN.name)) return
        viewModelScope.launch {
            try {
                withContext(IO) {
                    val originalUrl = WallParser.parseOriginalUrl(image.post)
                    val realImage = image.copy(url = originalUrl)
                    repository.update(realImage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        SPUtils.getInstance().put(VIEW_PAGE + repository.getType(), page)
    }

}