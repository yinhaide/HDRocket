package com.de.rocket.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.de.rocket.helper.ActivityHelper;
import com.de.rocket.helper.CrashHelper;
import com.de.rocket.helper.LocaleHelper;

public class RoApplication extends MultiDexApplication {

    //Application
    public static Application APPLICATION;

    @Override
    protected void attachBaseContext(Context context) {
        //多国语言适配
        super.attachBaseContext(LocaleHelper.attachBaseContext(context));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //多国语言适配
        LocaleHelper.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //存储当前的Application
        APPLICATION = this;
        //多国语言适配
        LocaleHelper.init(this);
        //崩溃异常的处理
        CrashHelper.getInstance().initCrash(this);
        //重写生命周期
        registerActivityLifecycleCallbacks(new ActivityHelper());
    }
}
