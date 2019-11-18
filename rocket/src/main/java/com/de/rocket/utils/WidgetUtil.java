package com.de.rocket.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.de.rocket.R;
import com.de.rocket.Rocket;

/**
 * 全局弹窗工具类，解决全局widget弹窗问题，用法
 * 1、显示 WidgetUtil.showGloaBall(NormalDialogWidget::new);
 * 2、隐藏 WidgetUtil.hideGloBall();
 * Created by haide.yin(haide.yin@tcl.com) on 2019/5/30 8:17.
 */
public class WidgetUtil {

    //用户缓存的ViewID
    private static final int GLOBAL_WIDGET_ID = R.id.rocket_global_widget_view;

    /**
     * 全局显示一个View
     *
     * @param activityResult 回调接口
     */
    public static void showGloaBall(ActivityResult activityResult) {
        Activity topActivity = Rocket.getTopActivity();
        //清除上个页面
        hideGloBall();
        if (activityResult != null && topActivity != null) {
            View view = activityResult.getView(topActivity);
            if (view != null) {
                ViewGroup contentView = topActivity.findViewById(android.R.id.content);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                view.setId(GLOBAL_WIDGET_ID);
                contentView.addView(view);
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏全局弹窗
     */
    public static void hideGloBall() {
        Activity activity = Rocket.getTopActivity();
        if (activity != null) {
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View fakeStatusBarView = decorView.findViewById(GLOBAL_WIDGET_ID);
            if (fakeStatusBarView != null) {
                decorView.removeView(fakeStatusBarView);//这一句待验证是否可以去掉
                contentView.removeView(fakeStatusBarView);
            }
        }
    }

    //通过反射得到前台活动的Activity
    /*@SuppressLint("PrivateApi")
    private static Activity getTopActivity() {
        // 要做适配,反射在10.0全部被google禁止, 会返回为null.
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (ClassNotFoundException | NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }*/


    /**
     * 读取当前Activity的回调
     */
    public interface ActivityResult {
        /**
         * 获取自定义的View并显示
         *
         * @param topActivity 返回当前的Activity
         * @return 返回当前的需要显示的View
         */
        View getView(Activity topActivity);
    }
}
