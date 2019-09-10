package com.dante.threedimens.data

import androidx.annotation.Keep

/**
 * @author Dante
 * 2019-08-23
 */
@Keep
data class AppInfo(
    val announcement: String,
    val apk_name: String,
    val forceUpdate: Boolean,
    val lastest_version: String,
    val lastest_version_code: Int,
    val message: String,
    val vip: List<String>
)