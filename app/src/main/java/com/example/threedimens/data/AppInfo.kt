package com.example.threedimens.data

data class AppInfo(
    val announcement: String,
    val apk_name: String,
    val forceUpdate: Boolean,
    val lastest_version: String,
    val lastest_version_code: Int,
    val message: String,
    val share_app_description: String,
    val vip: List<String>
)