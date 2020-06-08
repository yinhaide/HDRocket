package com.de.rocket.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.de.rocket.utils.RoLogUtil;

import java.lang.ref.WeakReference;

/**
 * Activity生命周期回调
 */
public class ActivityHelper implements Application.ActivityLifecycleCallbacks {

    //栈顶层的Activity,做了弱引用以及销毁处理,不会造成内存泄漏
    private static WeakReference<Activity> TOP_ACTIVITY;

    public ActivityHelper() {
        // 完善生命周期处理
    }

    /**
     * 获取栈顶的Activity
     */
    public static Activity getTopActivity() {
        return TOP_ACTIVITY.get();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        TOP_ACTIVITY = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        TOP_ACTIVITY = new WeakReference<>(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        TOP_ACTIVITY = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (TOP_ACTIVITY != null) {
            if (TOP_ACTIVITY.get().getLocalClassName().equals(activity.getLocalClassName())) {
                TOP_ACTIVITY = null;//防止内存泄漏
                RoLogUtil.v("ActivityHelper::onActivityDestroyed-->"+"TOP_ACTIVITY = null;//防止内存泄漏");
            }
        }
    }
}
