package com.example.threedimens.ui.picturelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.threedimens.R
import com.example.threedimens.data.Image
import com.example.threedimens.utils.load

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class PictureListAdapter(val onClick: (Image, View) -> Unit) :
    PagedListAdapter<Image, PictureListAdapter.PictureHolder>(IMAGE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.picture_list_item, parent, false)
        return PictureHolder(view)
    }

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PictureHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.image)
        fun bind(image: Image) {
            imageView.load(image.url)
            imageView.setOnClickListener { onClick(image, itemView) }
        }
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem.url == newItem.url
        }
    }
}