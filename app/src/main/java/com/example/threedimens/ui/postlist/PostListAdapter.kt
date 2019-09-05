package com.example.threedimens.ui.postlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.threedimens.R
import com.example.threedimens.data.Post
import com.example.threedimens.utils.load

/**
 * @author Dante
 * 2019-08-23
 */
class PostListAdapter(val onClick: (Post, View, Int) -> Unit) :
    ListAdapter<Post, PostListAdapter.PictureHolder>(IMAGE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.post_list_item, parent, false)
        return PictureHolder(view)
    }

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PictureHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.image)
        private val textView = view.findViewById<TextView>(R.id.title)
        fun bind(post: Post) {
            textView.text = post.title
            imageView.load(post.coverUrl, header = post.postUrl)
            imageView.setOnClickListener { onClick(post, itemView, adapterPosition) }
        }
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.postUrl == newItem.postUrl

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.coverUrl == newItem.coverUrl
        }
    }
}