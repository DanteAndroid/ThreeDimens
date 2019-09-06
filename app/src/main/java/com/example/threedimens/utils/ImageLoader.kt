package com.example.threedimens.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.threedimens.R
import com.example.threedimens.ui.detail.PictureDetailFragment
import com.example.threedimens.utils.widget.GlideApp
import com.example.threedimens.utils.widget.GlideRequest
import com.example.threedimens.utils.widget.RatioImageView

/**
 * @author Dante
 * 2019-08-27
 */

fun ImageView.load(
    url: String,
    header: String = "",
    /**
     * 是否显示转圈动画
     */
    animate: Boolean = false,
    /**
     * 加载原图
     */
    showOriginal: Boolean = false,
    thumbnail: GlideRequest<Bitmap>? = null,
    onResourceReady: () -> Unit = {},
    onLoadFailed: () -> Unit = {}
) {

    val requestOptions = RequestOptions()
        .dontTransform()
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
        .apply {
            if (showOriginal) {
                override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            } else {
                transition(BitmapTransitionOptions.withCrossFade())
            }
        }
        .thumbnail(thumbnail)
        .into(object : BitmapImageViewTarget(this) {

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                onLoadFailed()
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                super.onResourceReady(resource, transition)
                if (this@load is RatioImageView) {
                    this@load.setOriginalSize(resource.width, resource.height)
                }
                onResourceReady()
            }
        })
}

fun <T> PictureDetailFragment.getDelayedTransitionListener(): RequestListener<T> {
    return object : RequestListener<T> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
        ): Boolean {
            activity?.supportStartPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(
            resource: T?,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            activity?.supportStartPostponedEnterTransition()
            return false
        }
    }
}


