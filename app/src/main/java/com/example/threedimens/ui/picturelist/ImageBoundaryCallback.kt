package com.example.threedimens.ui.picturelist

import android.os.Handler
import android.text.format.DateUtils
import androidx.paging.PagedList
import com.example.threedimens.data.Image
import com.example.threedimens.utils.LOAD_LIST_RETRY_TIMES

/**
 * @author Dante
 * 2019-08-28
 */
class ImageBoundaryCallback(private val viewModel: PictureListViewModel) :
    PagedList.BoundaryCallback<Image>() {

    private var refreshTimes = 0
    private var loadMoreTimes = 0

    override fun onItemAtEndLoaded(itemAtEnd: Image) {
        super.onItemAtEndLoaded(itemAtEnd)
        if (refreshTimes < LOAD_LIST_RETRY_TIMES) {
            Handler().postDelayed({
                viewModel.loadMoreImages()
                refreshTimes++
            }, DateUtils.SECOND_IN_MILLIS)
        }
    }

    override fun onZeroItemsLoaded() {
        if (loadMoreTimes < LOAD_LIST_RETRY_TIMES) {
            Handler().postDelayed({
                viewModel.refreshImages()
                refreshTimes++
            }, DateUtils.SECOND_IN_MILLIS)
        }
    }

}