package com.de.rocket.utils;

import android.app.Activity;
import android.util.Log;
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
        hideGloBall(topActivity);
        if (activityResult != null && topActivity != null) {
            View view = activityResult.getView(topActivity);
            if (view != null) {
                //getDecorView获取DecorView是顶级View，它是一个FrameLayout布局，内部有titlebar和contentParent两个子元素。
                //titlebar：顶级标题栏，如果Activity设置了FEATURE_NO_ACTIONBAR，这个view就会消失，那么DecorView就只有mContentParent一个子View
                //contentParent：id是content，可以通过topActivity.findViewById(android.R.id.content)获取这个eView，们设置的Activity的setContentView(R.layout.main)的main.xml布局则是contentParent里面的一个子元素
                //如果我们添加的View想要忽略titlebar的话就需要用decorView来添加，不忽略的话可用contentParent添加
                ViewGroup decorView = (ViewGroup) topActivity.getWindow().getDecorView();
                //ViewGroup contentView = topActivity.findViewById(android.R.id.content);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                view.setId(GLOBAL_WIDGET_ID);
                decorView.addView(view);
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏全局弹窗
     */
    public static void hideGloBall(Activity activity) {
        if (activity != null) {
            //ViewGroup contentView = activity.findViewById(android.R.id.content);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            //如果我们addView是通过contentView添加的，则decorView和contentView都能找到这个View
            //如果我们addView是通过decorView添加的，只有decorView菜能找到这个View
            //所以删除的话只需要用decorView删除即可
            //View contentGlobalView = contentView.findViewById(GLOBAL_WIDGET_ID);
            View decorViewGlobalView = decorView.findViewById(GLOBAL_WIDGET_ID);
            if (decorViewGlobalView != null) {
                decorView.removeView(decorViewGlobalView);
                //contentView.removeView(decorViewGlobalView);
            }
        }
    }

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
