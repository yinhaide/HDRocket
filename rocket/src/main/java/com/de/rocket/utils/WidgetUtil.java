package com.de.rocket.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * 全局弹窗工具类，解决全局widget弹窗问题，用法
 * 1、显示 WidgetUtil.showGloaBal(activity, view, widgetID);
 * 2、隐藏 WidgetUtil.hideGloBal(activity, widgetID);
 * Created by haide.yin(haide.yin@tcl.com) on 2019/5/30 8:17.
 */
public class WidgetUtil {

    /**
     * 全局显示一个View
     *
     * @param activity 活动的activity
     * @param view 需要显示的View
     * @param widgetID ViewID
     * @param fadeDuraction 消失的渐变时间
     * @param viewCallback 重复View的处理策略回调
     */
    public static void showGloaBal(@NonNull Activity activity, View view, int widgetID, int fadeDuraction, ViewCallback viewCallback) {
        if (view != null) {//删除View期间不能添加View
            //activity.getWindow().getDecorView获取DecorView是顶级View，它是一个FrameLayout布局，内部有titlebar和contentParent两个子元素。
            //titlebar：顶级标题栏，如果Activity设置了FEATURE_NO_ACTIONBAR，这个view就会消失，那么DecorView就只有mContentParent一个子View
            //contentParent：id是content，可以通过topActivity.findViewById(android.R.id.content)获取这个eView，们设置的Activity的setContentView(R.layout.main)的main.xml布局则是contentParent里面的一个子元素
            //如果我们添加的View想要忽略titlebar的话就需要用decorView来添加，不忽略的话可用contentParent添加
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            //如果View存在就删除，防止重复添加
            View oldView = decorView.findViewById(widgetID);
            //是否要用户自己处理，true的话这里就不用处理了，外部处理就行
            boolean outerHandler = false;
            if (oldView != null) {
                outerHandler = onView(viewCallback, oldView);
            }
            if(!outerHandler){
                //ViewGroup contentView = topActivity.findViewById(android.R.id.content);
                //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                //view.setLayoutParams(params);
                if (oldView != null) {
                    decorView.removeView(oldView);
                }
                view.setId(widgetID);
                decorView.addView(view);
                //是否需要渐变
                if (fadeDuraction > 0) {
                    view.setVisibility(View.INVISIBLE);
                    //渐变显示
                    ViewAnimUtil.showFade(view, 0, 1, fadeDuraction);
                }
            }
        }
    }

    /**
     * 隐藏全局弹窗
     * @param activity 活动的activity
     * @param widgetID ViewID
     * @param fadeDuraction 消失的渐变时间
     */
    public static void hideGloBal(@NonNull Activity activity, int widgetID, int fadeDuraction) {
        //ViewGroup contentView = activity.findViewById(android.R.id.content);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        //如果我们addView是通过contentView添加的，则decorView和contentView都能找到这个View
        //如果我们addView是通过decorView添加的，只有decorView才能找到这个View
        //所以删除的话只需要用decorView删除即可
        //View contentGlobalView = contentView.findViewById(GLOBAL_WIDGET_ID);
        View view = decorView.findViewById(widgetID);
        if (view != null) {
            //是否需要渐变
            if (fadeDuraction > 0) {
                //渐变显示
                ViewAnimUtil.hideFade(view, 1, 0, fadeDuraction);
                decorView.removeView(view);
            } else {
                decorView.removeView(view);
            }
        }
    }

    /**
     * 返回旧的View，用户决定他的操作
     */
    private static boolean onView(ViewCallback viewCallback, View view) {
        if (viewCallback != null) {
            return viewCallback.onView(view);
        }
        return false;
    }

    /**
     * GlobalView回调,返回旧的View，用户决定他的操作
     */
    public interface ViewCallback {

        /**
         * GlobalView回调,返回旧的View，用户决定他的操作
         * @param view 重复的View
         * @return true:外部自行处理；false:内部处理
         */
        boolean onView(View view);
    }
}
