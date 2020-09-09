package com.dante.threedimens.utils

import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.view.View
import com.blankj.utilcode.util.SPUtils
import com.dante.threedimens.R
import org.jetbrains.anko.design.snackbar

/**
 * @author Du Wenyu
 * 2019-09-16
 */
object SecretModeHelper {

    private const val SECRET_MODE = "secret_mode"

    private var times: Int = 0
    private var lastTime: Long = 0

    fun attachSecretModeClick(view: View) {
        view.setOnClickListener {
            if (times > 3) {
                val isSecretMode = isSecretMode()
                SPUtils.getInstance().put(SECRET_MODE, !isSecretMode)
                if (isSecretMode) {
                    times = 0
                    view.snackbar(R.string.secret_mode_closed)
                } else {
                    times = 0
                    view.snackbar(R.string.secret_mode_opened)
                }
            }
            if (lastTime == 0L) {
                lastTime = System.currentTimeMillis()
            }
            if (System.currentTimeMillis() - lastTime < 500) {
                lastTime = System.currentTimeMillis()
                times++
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    times = 0
                    lastTime = 0L
                }, DateUtils.SECOND_IN_MILLIS)
            }
        }
    }

    fun isSecretMode(): Boolean = SPUtils.getInstance().getBoolean(SECRET_MODE)


}