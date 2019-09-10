package com.dante.threedimens.utils.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dante.threedimens.ui.main.AppViewModel

/**
 * @author Dante
 * 2019-08-23
 */
class AppViewModelFactory(private val activity: Activity) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(activity) as T
    }
}