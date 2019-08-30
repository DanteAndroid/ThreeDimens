package com.example.threedimens.ui.detail


import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.transition.doOnEnd
import androidx.core.view.ViewCompat
import com.example.base.base.BaseFragment
import com.example.threedimens.R
import com.example.threedimens.utils.UiUtil
import com.example.threedimens.utils.load
import kotlinx.android.synthetic.main.fragment_picture_detail.*

class PictureDetailFragment private constructor() : BaseFragment() {

    private val url: String by lazy {
        arguments!!.getString(ARG_IMAGE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_detail, container, false)
    }

    override fun initView() {
        ViewCompat.setTransitionName(detailImage, url)
        detailImage.setOnClickListener { UiUtil.toggleSystemUI(it) }
        detailImage.load(url, true) {
            activity?.supportStartPostponedEnterTransition()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.sharedElementEnterTransition = TransitionSet()
                .addTransition(android.transition.ChangeImageTransform())
                .addTransition(android.transition.ChangeBounds())
                .apply {
                    doOnEnd { detailImage.load(url) }
                }
            activity?.window?.enterTransition = Fade().apply {
                excludeTarget(android.R.id.statusBarBackground, true)
                excludeTarget(android.R.id.navigationBarBackground, true)
                excludeTarget(R.id.action_bar_container, true)
            }
        } else {
            detailImage.load(url)
        }
    }

    companion object {
        private const val ARG_IMAGE = "image"

        fun newInstance(imageUrl: String): PictureDetailFragment {
            return PictureDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE, imageUrl)
                }
            }
        }
    }

}
