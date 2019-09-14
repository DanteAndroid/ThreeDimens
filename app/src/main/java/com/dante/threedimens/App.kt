package com.dante.threedimens

import androidx.appcompat.app.AppCompatDelegate
import com.blankj.utilcode.util.Utils
import com.bugtags.library.Bugtags
import com.dante.base.base.BaseApplication
import com.github.anrwatchdog.ANRWatchDog

/**
 * @author Dante
 * 2019-08-23
 */
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        Utils.init(this)
        Bugtags.start("0d6e2cc47a7db77bbde298fff0ff02df", this, Bugtags.BTGInvocationEventNone)
        ANRWatchDog().start()
        ANRWatchDog().setANRListener {
            Bugtags.sendException(it.cause)
        }
    }
}
