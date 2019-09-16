package com.dante.threedimens.ui.picturelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dante.threedimens.R
import com.dante.threedimens.data.Image
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.utils.load

/**
 * @author Dante
 * 2019-08-23
 */
class PictureListAdapter(
    val type: ApiType,
    val onClick: (Image, View, Int) -> Unit
) :
    PagedListAdapter<Image, PictureListAdapter.PictureHolder>(IMAGE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
        val layoutId = if (type.path.isEmpty() && type.site != ApiType.Site.WALLHAVEN) {
            R.layout.picture_list_item
        } else {
            when (type.site) {
                ApiType.Site.WALLHAVEN -> R.layout.picture_list_item_fix_small
                ApiType.Site.MEIZITU, ApiType.Site.MTL -> R.layout.picture_list_item_fix_big
                else -> R.layout.picture_list_item
            }
        }
        val view =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return PictureHolder(view)
    }

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PictureHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.image)
        fun bind(image: Image) {
            setIndexText()
            imageView.setOnClickListener(null)
            imageView.load(image.url, header = image.post, onResourceReady = {
                imageView.setOnClickListener { onClick(image, itemView, adapterPosition) }
            }, onLoadFailed = {
                imageView.setOnClickListener { onClick(image, itemView, adapterPosition) }
            })

        }


        private fun setIndexText() {
            if (type.site == ApiType.Site.MEIZITU) {
                itemView.findViewById<TextView>(R.id.num).text = (adapterPosition + 1).toString()
            }
        }
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
                true
        }
    }
}