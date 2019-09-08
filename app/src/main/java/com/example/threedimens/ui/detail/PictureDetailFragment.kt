package com.example.threedimens.ui.detail


import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.Fade
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.base.base.BaseFragment
import com.example.threedimens.R
import com.example.threedimens.data.Image
import com.example.threedimens.utils.LOAD_PICTURE_RETRY_TIMES
import com.example.threedimens.utils.UiUtil
import com.example.threedimens.utils.getDelayedTransitionListener
import com.example.threedimens.utils.load
import com.example.threedimens.utils.widget.GlideApp
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.fragment_picture_detail.*

class PictureDetailFragment private constructor() : BaseFragment() {

    private val id: String by lazy {
        arguments!!.getString(ARG_ID)!!
    }
    private val showTransition: Boolean by lazy {
        arguments!!.getBoolean(ARG_TRANSITION, false)
    }

    private lateinit var viewModel: PictureViwerViewModel
    private var retryTimes: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_detail, container, false)
    }

    override fun initView() {
        viewModel = (activity as PictureViewerActivity).viewModel
        viewModel.getImage(id).observe(this, Observer { image ->
            ViewCompat.setTransitionName(detailImage, image.url)
            detailImage?.setMaxZoomRatio(2.0f)
            detailImage.setOnClickListener { UiUtil.toggleSystemUI(it) }
            setProgressIndicator(true)
            if (showTransition) {
                // 直接点进来的才显示 SharedElement 动画
                loadWithTransition(image)
            } else {
                load(image)
            }
        })
    }

    private fun loadWithTransition(image: Image) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            load(image, true)
            val transitionSet = TransitionSet()
                .addTransition(ChangeImageTransform())
                .addTransition(ChangeBounds())
            activity?.window?.sharedElementEnterTransition = transitionSet
            activity?.window?.enterTransition = Fade().apply {
                excludeTarget(android.R.id.statusBarBackground, true)
                excludeTarget(android.R.id.navigationBarBackground, true)
                excludeTarget(R.id.action_bar_container, true)
            }

        } else {
            load(image)
        }
    }

    private fun load(image: Image, showTransition: Boolean = false) {
        val thumbnail = GlideApp.with(this)
            .asBitmap()
            .onlyRetrieveFromCache(true)
            .load(image.url).apply {
                if (showTransition)
                    listener(getDelayedTransitionListener())
            }
        detailImage.load(
            if (image.originalUrl.isEmpty()) image.url else image.originalUrl,
            showOriginal = true,
            thumbnail = thumbnail,
            onResourceReady = {
                setProgressIndicator(false)
            },
            onLoadFailed = {
                setProgressIndicator(false)
                if (retryTimes < LOAD_PICTURE_RETRY_TIMES) {
                    retryTimes++
                    viewModel.fetchRealUrl(image)
                }
            }
        )
    }

    private fun setProgressIndicator(show: Boolean) {
//        detailImage.isZoomEnabled = true
        println("setProgress $show")
        progress.isVisible = show
    }

    fun restoreImage() {
        if (detailImage?.isZoomed == true) {
            (detailImage as TouchImageView).setZoom(1.0f)
        }
    }

    companion object {
        private const val ARG_ID = "image"
        private const val ARG_TRANSITION = "position"

        fun newInstance(id: String, showTransition: Boolean): PictureDetailFragment {
            return PictureDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                    putBoolean(ARG_TRANSITION, showTransition)
                }
            }
        }
    }

}
