package com.dante.threedimens.utils

import android.graphics.Bitmap
import android.text.format.DateUtils
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dante.threedimens.R
import com.dante.threedimens.ui.detail.PictureDetailFragment
import com.dante.threedimens.utils.widget.GlideApp
import com.dante.threedimens.utils.widget.GlideRequest
import com.dante.threedimens.utils.widget.RatioImageView


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
            if (showOriginal) {
                // 显示原图时提高一倍的超时
                timeout((5 * DateUtils.SECOND_IN_MILLIS).toInt())
            } else {
                placeholder(R.drawable.placeholder)
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
        .asBitmap()
        .load(getUrlWithHeader(url))
        .apply(requestOptions)
        .apply {
            if (showOriginal) {
                override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .error(R.drawable.placeholder)
            } else {
                transition(BitmapTransitionOptions.withCrossFade())
            }
        }
        .thumbnail(thumbnail)
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadFailed()
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let {
                    if (this@load is RatioImageView) {
                        this@load.setOriginalSize(resource.width, resource.height)
                    }
                }
                onResourceReady()
                return false
            }

        })
        .into(this)
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


