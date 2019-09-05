package com.example.threedimens

import com.blankj.utilcode.util.Utils
import com.bugtags.library.Bugtags
import com.example.base.base.BaseApplication
import com.github.anrwatchdog.ANRWatchDog

/**
 * @author Dante
 * 2019-08-23
 */
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        ANRWatchDog().start()
        println("Init Application")
        Bugtags.start("0d6e2cc47a7db77bbde298fff0ff02df", this, Bugtags.BTGInvocationEventNone)
    }
}
