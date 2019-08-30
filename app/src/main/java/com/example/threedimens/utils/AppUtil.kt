package com.example.threedimens.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.example.threedimens.BuildConfig
import com.example.threedimens.R
import moe.feng.alipay.zerosdk.AlipayZeroSdk

/**
 * Created by Dante on 2016/2/19.
 */
object AppUtil {


    fun openAppInfo(context: Context) {
        //redirect user to app Settings
        val i = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + context.applicationContext.packageName)
        context.startActivity(i)
    }

    fun isIntentSafe(intent: Intent): Boolean {
        val packageManager = Utils.getApp().packageManager
        val activities =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return activities.size > 0
    }

    fun goMarket(activity: AppCompatActivity) {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
            )
            activity.startActivity(intent)
        } catch (anfe: android.content.ActivityNotFoundException) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.coolapk.com/apk/" + BuildConfig.APPLICATION_ID)
            )
            activity.startActivity(intent)
        }
    }


    fun donate(activity: AppCompatActivity) {
        val deviceId = SPUtils.getInstance().getString("deviceId")
        val cm = Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        cm.setPrimaryClip(ClipData.newPlainText("text", deviceId))

        if (AlipayZeroSdk.hasInstalledAlipayClient(activity.applicationContext)) {
            if (deviceId.isEmpty()) {
                ToastUtils.showShort(R.string.device_id_uncopied)
            } else {
                ToastUtils.showLong(R.string.device_id_copied)
                Handler().postDelayed({
                    AlipayZeroSdk.startAlipayClient(
                        activity,
                        "fkx06410coelwa7winexd90"
                    )
                }, 500)
            }
        } else {
            ToastUtils.showShort(R.string.alipay_not_found)
        }
    }
}
