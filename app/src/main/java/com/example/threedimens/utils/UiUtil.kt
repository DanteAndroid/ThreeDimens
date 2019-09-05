package com.example.threedimens.utils

import android.view.View
import androidx.core.view.GravityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.threedimens.MainDrawerActivity
import com.example.threedimens.R
import kotlinx.android.synthetic.main.activity_main_drawer.*
import kotlinx.android.synthetic.main.app_bar_main_drawer.*

/**
 * @author Dante
 * 2019-08-25
 */
object UiUtil {

    private var isUiShown = true
    private const val SYSTEM_UI_SHOW = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    private const val SYSTEM_UI_HIDE = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)

    private fun showSystemUI(view: View) {
        isUiShown = true
        view.systemUiVisibility = SYSTEM_UI_SHOW
    }

    private fun hideSystemUI(view: View) {
        isUiShown = false
        view.systemUiVisibility = SYSTEM_UI_HIDE
    }

    fun toggleSystemUI(view: View) {
        if (isUiShown) {
            hideSystemUI(view)
        } else {
            showSystemUI(view)
        }
    }

    // 延迟隐藏，防止圆圈乱闪
    fun smoothHideSwipeRefresh(swipeRefresh: SwipeRefreshLayout) {
        swipeRefresh.postDelayed({
            swipeRefresh.isRefreshing = false
        }, 300)
    }
}

fun MainDrawerActivity.setBack(enable: Boolean) {
    if (enable) {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
            setBack(false)
        }
    } else {
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
    }
}