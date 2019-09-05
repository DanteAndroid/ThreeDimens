package com.example.threedimens.ui.picturelist

import androidx.paging.PagedList
import com.example.threedimens.data.Image

/**
 * @author Dante
 * 2019-08-28
 */
class ImageBoundaryCallback(private val viewModel: PictureListViewModel) :
    PagedList.BoundaryCallback<Image>() {

    override fun onItemAtFrontLoaded(itemAtFront: Image) {
        super.onItemAtFrontLoaded(itemAtFront)
        viewModel.refreshImages()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Image) {
        super.onItemAtEndLoaded(itemAtEnd)
        viewModel.loadMoreImages()
    }

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        viewModel.refreshImages()
    }

}