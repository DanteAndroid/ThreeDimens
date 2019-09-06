package com.example.threedimens.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.threedimens.R
import com.example.threedimens.utils.widget.GlideApp
import com.example.threedimens.utils.widget.RatioImageView

/**
 * @author Dante
 * 2019-08-27
 */

fun ImageView.load(
    url: String,
    header: String = "",
    /**
     * 仅从缓存中读取，缓存里没有则加载失败
     */
    loadOnlyFromCache: Boolean = false,
    /**
     * 是否显示转圈动画
     */
    animate: Boolean = false,
    /**
     * 加载原图
     */
    showOriginal: Boolean = false,
    onLoadingFinished: () -> Unit = {},
    onLoadingFailed: () -> Unit = {}
) {

    val requestOptions = RequestOptions()
        .dontTransform()
        .onlyRetrieveFromCache(loadOnlyFromCache)
        .apply {
            if (animate) placeholder(R.drawable.loading_animation)
            if (!showOriginal) placeholder(R.drawable.placeholder)
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
        .asBitmap()
        .load(getUrlWithHeader(url))
        .apply(requestOptions)
//        .error(GlideApp.with(this).load(getUrlWithHeader(url)))
//        .listener(listener)
        .apply {
            if (!loadOnlyFromCache) {
                transition(BitmapTransitionOptions.withCrossFade())
            }
            if (showOriginal) {
//                override(
//                    com.bumptech.glide.request.target.Target.SIZE_ORIGINAL,
//                    com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
//                )
            }
        }
        .into(object : BitmapImageViewTarget(this) {

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                onLoadingFinished()
                onLoadingFailed()
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                super.onResourceReady(resource, transition)
                if (this@load is RatioImageView) {
                    this@load.setOriginalSize(resource.width, resource.height)
                }
                onLoadingFinished()
            }
        })
}

