package com.example.threedimens.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.example.threedimens.BuildConfig
import com.example.threedimens.R
import moe.feng.alipay.zerosdk.AlipayZeroSdk
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

/**
 * Created by Dante on 2016/2/19.
 */
object AppUtil {


    fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        val unsafeTrustManager = createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)
        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
            .hostnameVerifier(HostnameVerifier { p0, p1 -> true })
    }

    private fun createUnsafeTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<out X509Certificate>? {
                return emptyArray()
            }
        }
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
