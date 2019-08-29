package com.example.threedimens.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.threedimens.R
import com.example.threedimens.widget.GlideApp

/**
 * @author Du Wenyu
 * 2019-08-27
 */

fun ImageView.load(
    url: String,
    loadOnlyFromCache: Boolean = false,
    onLoadingFinished: () -> Unit = {}
) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }
    }

    val requestOptions = RequestOptions()
        .placeholder(R.drawable.loading_animation)
        .error(R.drawable.placeholder)
        .dontTransform()
        .onlyRetrieveFromCache(loadOnlyFromCache)

    GlideApp.with(this)
        .load(url)
        .apply(requestOptions)
        .listener(listener)
        .into(this)
}