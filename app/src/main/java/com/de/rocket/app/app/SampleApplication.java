package com.de.rocket.app.app;

import android.support.multidex.MultiDexApplication;

import com.de.rocket.Rocket;

/**
 * 本类给出一个Application的Demo，当然开发者也可以直接extends RoApplication，最终只需要调用必须的Rocket.init(application)即可；
 * Created by haide.yin(haide.yin@tcl.com) on 2020/6/7 17:43
 */
public class SampleApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //架构必须要初始化操作
        Rocket.init(this);
    }
}
