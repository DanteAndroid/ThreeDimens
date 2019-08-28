package com.example.threedimens;

import com.blankj.utilcode.util.Utils;
import com.example.base.base.BaseApplication;

/**
 * @author Du Wenyu
 * 2019-08-23
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
