package com.example.base.net

/**
 * @author Dante
 * 2019-08-19
 */
open class BaseListResponse<out T>(
    val code: String? = null,
    val msg: String? = null,
    val data: List<T>? = null
)

open class BaseResponse<out T>(
    val code: String? = null,
    val msg: String? = null,
    val data: T? = null
)