package com.dante.base.base

import android.app.Application

/**
 * @author Dante
 * 2019-08-19
 */
abstract class BaseApplication : Application() {

    companion object {
        private lateinit var application: Application
        fun instance() = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}