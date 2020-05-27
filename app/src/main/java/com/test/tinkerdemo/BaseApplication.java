package com.test.tinkerdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.test.fixlibrary.FixDexUtils;

/**
 *模拟分包 放在主包
 * 支持分包
 */
public class BaseApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //安装分包配置
        MultiDex.install(this);
        FixDexUtils.loadDexFile(this);
    }
}
