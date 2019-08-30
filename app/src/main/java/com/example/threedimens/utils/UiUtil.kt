package com.example.threedimens.utils

import android.view.View

/**
 * @author Du Wenyu
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

}