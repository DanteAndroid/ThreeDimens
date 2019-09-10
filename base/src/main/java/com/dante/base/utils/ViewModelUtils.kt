package com.dante.base.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dante.base.base.BaseActivity

/**
 * @author Dante
 * 2019-08-21
 */

inline fun <reified T : ViewModel> BaseActivity.getViewModel(): T {
    return ViewModelProvider.AndroidViewModelFactory(application).create(T::class.java)
}

