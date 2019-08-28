package com.example.threedimens.ui.picturelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.SPUtils
import com.example.base.base.BaseStatusVM
import com.example.base.base.LoadStatus
import com.example.threedimens.data.Image
import kotlinx.coroutines.launch

/**
 * 维护每个 tab 下的页面及其数据
 */
class PictureListViewModel(private val repository: ImageRepository) : BaseStatusVM() {

    private var page = SPUtils.getInstance().getInt("page", 1)

    val images: LiveData<List<Image>> = repository.getImages()

    init {
        refreshImages()
    }

    fun refreshImages() {
        viewModelScope.launch {
            fetchImages(pageNum = 1)
        }
    }

    fun loadMoreImages() {
        viewModelScope.launch {
            fetchImages(page++)
            println("LoadPage $page")
        }
    }

    fun isLoadMore() = page > 1

    private suspend fun fetchImages(pageNum: Int) {
        try {
            setStatus(LoadStatus.LOADING)
            val result = repository.fetchImages(pageNum)

            check(result.isNotEmpty()) { "No images in result" }
            println("fetch ${result.size} ${result.first()}")
            repository.insert(result)
            setStatus(LoadStatus.DONE)
            println("fetch insert ${result.size} ${result.first().type}")

        } catch (e: Exception) {
            e.printStackTrace()
            setStatus(LoadStatus.ERROR)
            if (page > 1) page--
        }
    }

    override fun onCleared() {
        super.onCleared()
        SPUtils.getInstance().put("page", page)
    }

}