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
import androidx.core.transition.doOnEnd
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.example.base.base.BaseFragment
import com.example.threedimens.R
import com.example.threedimens.data.Image
import com.example.threedimens.utils.UiUtil
import com.example.threedimens.utils.load
import kotlinx.android.synthetic.main.fragment_picture_detail.*

class PictureDetailFragment private constructor() : BaseFragment() {

    private val id: String by lazy {
        arguments!!.getString(ARG_ID)!!
    }


    private val showTransition: Boolean by lazy {
        arguments!!.getBoolean(ARG_TRANSITION, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_detail, container, false)
    }

    override fun initView() {
        (activity as PictureViewerActivity).viewModel.getImage(id).observe(this, Observer { image ->
            ViewCompat.setTransitionName(detailImage, image.url)
            detailImage.setMaxZoomRatio(2.0f)
            detailImage.setOnClickListener { UiUtil.toggleSystemUI(it) }
            if (showTransition) {
                // 直接点进来的才显示 SharedElement 动画
                loadWithTransition(image)
            } else {
                simpleLoad(image)
            }
        })

    }

    private fun loadWithTransition(image: Image) {
        detailImage.load(image.url, loadOnlyFromCache = true, onLoadingFinished = {
            activity?.supportStartPostponedEnterTransition()
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.sharedElementEnterTransition = TransitionSet()
                .addTransition(ChangeImageTransform())
                .addTransition(ChangeBounds()).apply {
                    doOnEnd {
                        println("loadOnEnd ${image.originalUrl}")
                        detailImage?.load(
                            if (image.originalUrl.isEmpty()) image.url else image.originalUrl,
                            showOriginal = true
                        )
                    }
                }
            activity?.window?.enterTransition = Fade().apply {
                excludeTarget(android.R.id.statusBarBackground, true)
                excludeTarget(android.R.id.navigationBarBackground, true)
                excludeTarget(R.id.action_bar_container, true)
            }
        } else {
            simpleLoad(image)
        }
    }

    private fun simpleLoad(image: Image) {
        detailImage.load(
            if (image.originalUrl.isEmpty()) image.url else image.originalUrl,
            showOriginal = true,
            animate = true
        )
    }

    fun restoreImage() {
        detailImage?.also {
            if (it.isZoomed) {
                detailImage?.setZoom(1.0f)
            }
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
