package com.example.threedimens.ui.picturelist

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.example.threedimens.R
import com.example.threedimens.data.Image
import com.example.threedimens.utils.load

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class PictureListAdapter : BaseQuickAdapter<Image, BaseViewHolder>(R.layout.picture_list_item) {


    override fun convert(helper: BaseViewHolder, image: Image) {
        val imageView = helper.getView(R.id.image) as ImageView
        imageView.load(image.url)

//        Glide.with(mContext)
//            .load(image.url)
//            .placeholder(R.drawable.loading_animation)
//            .error(R.drawable.ic_broken_image)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(imageView)
    }

    fun submitList(images: List<Image>) {
        setNewDiffData(object : BaseQuickDiffCallback<Image>(images) {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.url == newItem.url
            }
        })
    }

}