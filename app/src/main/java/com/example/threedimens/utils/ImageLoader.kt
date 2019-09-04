package com.example.threedimens.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.threedimens.R
import com.example.threedimens.utils.widget.GlideApp

/**
 * @author Du Wenyu
 * 2019-08-27
 */

fun ImageView.load(
    url: String,
    header: String = "",
    loadOnlyFromCache: Boolean = false,
    animate: Boolean = false,
    showOriginal: Boolean = false,
    onLoadingFinished: () -> Unit = {},
    onLoadingFailed: () -> Unit = {}
) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            onLoadingFailed()
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
        .error(R.drawable.placeholder)
        .dontTransform()
        .onlyRetrieveFromCache(loadOnlyFromCache)
        .apply {
            if (!animate) {
                placeholder(R.drawable.loading_animation)
            }
        }

    fun getUrlWithHeader(url: String): GlideUrl {
        return GlideUrl(
            url, LazyHeaders.Builder()
                .addHeader("Referer", header)
                .addHeader("User-Agent", PC_USER_AGENT)
                .build()
        )
    }
    GlideApp.with(this)
        .load(getUrlWithHeader(url))
        .apply(requestOptions)
//        .error(GlideApp.with(this).load(getUrlWithHeader(url)))
        .listener(listener)
        .apply {
            if (!loadOnlyFromCache) {
                transition(DrawableTransitionOptions.withCrossFade())
            }
            if (showOriginal) {
//                override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            }
        }
        .into(this)
}

