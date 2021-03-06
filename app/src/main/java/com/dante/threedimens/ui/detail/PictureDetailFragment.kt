package com.dante.threedimens.ui.detail


import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.Fade
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.dante.base.base.BaseFragment
import com.dante.threedimens.R
import com.dante.threedimens.data.Image
import com.dante.threedimens.ui.main.ApiType
import com.dante.threedimens.utils.*
import com.dante.threedimens.utils.widget.GlideApp
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.fragment_picture_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.selector

class PictureDetailFragment private constructor() : BaseFragment() {

    var dontShowTransition: Boolean = false
    private val id: String by lazy {
        requireArguments().getString(ARG_ID)!!
    }
    private val showTransition: Boolean by lazy {
        requireArguments().getBoolean(ARG_TRANSITION, false)
    }

    private lateinit var viewModel: PictureViwerViewModel
    private lateinit var detailImage: ImageView
    private var retryTimes: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_detail, container, false)
    }

    override fun initView() {
        detailImage = requireView().findViewById(R.id.detailImage)
        viewModel = (activity as PictureViewerActivity).viewModel
        if (detailImage is TouchImageView) {
            (detailImage as TouchImageView).setMaxZoomRatio(2.0f)
        }
        viewModel.getImage(id).observe(this, { image ->
            ViewCompat.setTransitionName(detailImage, image.url)
            detailImage.setOnClickListener { UiUtil.toggleSystemUI(it) }
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
        setProgressIndicator(true)
        val thumbnail = GlideApp.with(this)
            .asBitmap()
            .onlyRetrieveFromCache(true)
            .load(image.url).apply {
                if (showTransition)
                    listener(getDelayedTransitionListener())
            }
        try {
            detailImage.load(
                if (image.originalUrl.isEmpty()) image.url else image.originalUrl,
                showOriginal = true,
                thumbnail = thumbnail,
                onResourceReady = {
                    setProgressIndicator(false)
                    onLoadSuccess(image)
                },
                onLoadFailed = {
                    if (retryTimes < LOAD_PICTURE_RETRY_TIMES) {
                        retryTimes++
                        viewModel.fetchRealUrl(image)
                    } else {
                        setProgressIndicator(false)
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun onLoadSuccess(image: Image) {
        // 壁纸加载大图后不需要显示 transition（因为和缩略图比例不同）
        dontShowTransition = image.type.contains(ApiType.Site.WALLHAVEN.name)

        (detailImage as? TouchImageView)?.isZoomEnabled = true
        detailImage.setOnLongClickListener {
            it.context.apply {
                val items = listOf(
                    getString(R.string.save_img),
                    getString(R.string.share)
                )
                selector(items = items) { _, i ->
                    when (i) {
                        0 -> downloadPicture(image, showHint = true)
                        1 -> sharePicture(image)
                    }
                }
            }
            return@setOnLongClickListener true
        }
    }

    private fun downloadPicture(image: Image, showHint: Boolean = false) {
        val file = doAsyncResult {
            GlideApp.with(this@PictureDetailFragment)
                .download(if (image.originalUrl.isEmpty()) image.url else image.originalUrl)
                .submit().get()

        }.get()
        val result = saveImage(image, file)
        if (result) {
            if (showHint) {
                detailImage.snackbar(R.string.save_img_success)
            }
        } else {
            file.delete()
        }
    }

    private fun sharePicture(image: Image) {
        context?.let {
            val file = getImageFile(image)
            if (file.exists()) {
                Share.shareImage(it, file)
            } else {
                downloadPicture(image)
                sharePicture(image)
            }
        }
    }

    private fun setProgressIndicator(show: Boolean) {
        if (show) {
            progress?.show()
        } else {
            progress?.hide()
        }
    }

    fun restoreImage() {
        if (detailImage is TouchImageView) {
            if ((detailImage as TouchImageView).isZoomed) {
                (detailImage as TouchImageView).setZoom(1.0f)
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
