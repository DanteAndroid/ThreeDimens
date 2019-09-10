package com.dante.threedimens.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.dante.threedimens.BuildConfig
import com.dante.threedimens.R
import com.dante.threedimens.data.AppInfo
import com.dante.threedimens.net.API
import com.dante.threedimens.net.NetManager
import com.dante.threedimens.utils.AppUtil
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.toast
import java.io.File

/**
 * @author Dante
 * 2019-09-10
 */
class AppViewModel(val context: Activity) : ViewModel() {

    private val adapter: JsonAdapter<AppInfo> by lazy {
        Moshi.Builder().build().adapter(AppInfo::class.java)
    }

    private var receiver: BroadcastReceiver? = null

    fun check() {
        viewModelScope.launch {
            val data = withContext(IO) {
                NetManager.appApi.getAppInfo().execute().body()
            }
            startRoutine(data)
        }
    }

    private fun startRoutine(data: AppInfo?) {
        var appInfo = data
        if (appInfo == null) {
            appInfo = adapter.fromJson(SPUtils.getInstance().getString("AppInfo"))
        } else {
            SPUtils.getInstance().put("AppInfo", adapter.toJson(appInfo))
        }
        appInfo?.let {
            showUpdateDialog(it)
        }
    }

    private fun showUpdateDialog(appInfo: AppInfo) {
        if (BuildConfig.VERSION_CODE < appInfo.lastest_version_code) {
            context.apply {
                AlertDialog.Builder(context).setTitle(R.string.new_version)
                    .setCancelable(!appInfo.forceUpdate)//需要更新就不可取消
                    .setMessage(appInfo.message)
                    .setNegativeButton(R.string.go_market) { _, _ ->
                        AppUtil.goMarket(this)
                    }
                    .setPositiveButton(R.string.update) { _, _ ->
                        download(appInfo)
                        toast(R.string.download_start)
                    }.show()
            }
        } else {
            showAnnouncement(appInfo.announcement)
        }
    }

    private fun showAnnouncement(announcement: String) {
        if (announcement.isNotEmpty() && SPUtils.getInstance().getBoolean(announcement, true)) {
            context.apply {
                alert(message = announcement) {
                    okButton {
                        SPUtils.getInstance().put(announcement, false)
                    }
                }.show()
            }
        }
    }

    private fun download(appInfo: AppInfo) {
        val fileName = "v${appInfo.lastest_version}-${appInfo.apk_name}"
        val destFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        val url = "${API.DOWNLOAD_BASE}${appInfo.lastest_version}/${appInfo.apk_name}"
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(context.getString(R.string.downloading, appInfo.lastest_version))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(destFile))

        val id = (context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                if (id == downloadId) {
                    println("DownloadManager success $id")
                    AppUtils.installApp(destFile)
                }
            }
        }
        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onCleared() {
        super.onCleared()
        receiver?.also {
            context.unregisterReceiver(it)
        }
    }
}