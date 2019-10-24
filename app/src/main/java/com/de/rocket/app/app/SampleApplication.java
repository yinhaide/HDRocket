package com.de.rocket.app.app;

import com.de.rocket.Rocket;
import com.de.rocket.app.RoApplication;

/**
 * The type Sample application.
 */
public class SampleApplication extends RoApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //不检测Uri,7.0以上系统访问文件权限
        Rocket.detectFileUriExposure();
    }
}
