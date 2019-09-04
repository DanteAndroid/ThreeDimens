package com.example.threedimens.ui.picturelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.threedimens.R
import com.example.threedimens.data.AppDatabase
import com.example.threedimens.data.Image
import com.example.threedimens.main.ApiType
import com.example.threedimens.parse.WallParser
import com.example.threedimens.utils.load
import org.jetbrains.anko.runOnUiThread
import kotlin.concurrent.thread

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class PictureListAdapter(val onClick: (Image, View, Int) -> Unit) :
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
            setNumber(image)
            imageView.load(image.url, header = image.post, onLoadingFailed = {
                println("parseRealImage ${image.url}")
                fetchRealUrl(image, imageView)
            })
            imageView.setOnClickListener { onClick(image, itemView, adapterPosition) }
        }

        private fun fetchRealUrl(image: Image, imageView: ImageView) {
            try {
                thread {
                    val originalUrl = WallParser.parseOriginalUrl(image.post)
                    val realImage = image.copy(url = originalUrl)
                    AppDatabase.getInstance(itemView.context).imageDao().insert(realImage)
                    itemView.context.runOnUiThread {
                        println("parseRealImage ${realImage} ${image}")
                        notifyItemChanged(adapterPosition)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem.url == newItem.url
        }
    }
}