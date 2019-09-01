package com.example.threedimens

import com.blankj.utilcode.util.Utils
import com.example.base.base.BaseApplication
import com.github.anrwatchdog.ANRWatchDog

/**
 * @author Du Wenyu
 * 2019-08-23
 */
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        ANRWatchDog().start()
    }
}
