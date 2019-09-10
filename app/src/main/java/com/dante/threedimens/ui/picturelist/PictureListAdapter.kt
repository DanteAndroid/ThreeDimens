package com.dante.threedimens.ui.picturelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dante.threedimens.R
import com.dante.threedimens.data.Image
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.utils.load
import com.dante.threedimens.utils.widget.RatioImageView

/**
 * @author Dante
 * 2019-08-23
 */
class PictureListAdapter(
    val onClick: (Image, View, Int) -> Unit
) :
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
        private val imageView = view.findViewById<RatioImageView>(R.id.image)
        fun bind(image: Image) {
            setNumber(image)
            imageView.load(image.url, header = image.post)
            imageView.setOnClickListener { onClick(image, itemView, adapterPosition) }
        }


        private fun setNumber(image: Image) {
            if (image.type.contains(ApiType.Site.MEIZITU.name)) {
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