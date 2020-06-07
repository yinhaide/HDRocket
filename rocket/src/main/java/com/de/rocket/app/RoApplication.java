package com.de.rocket.app;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.de.rocket.Rocket;

/**
 * 本类给出一个Application的Demo，当然开发者也可以不用这个，使用自己的，只需要调用必须的Rocket.init(application)即可；
 * Created by haide.yin(haide.yin@tcl.com) on 2019/6/24 17:43.
 */
public class RoApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context context) {
        //多国语言适配
        super.attachBaseContext(Rocket.attachBaseContext(context));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //多国语言适配
        Rocket.onConfigurationChanged(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Rocket.init(this);
    }
}
