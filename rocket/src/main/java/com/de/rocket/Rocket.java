package com.de.rocket;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import com.de.rocket.app.RoApplication;
import com.de.rocket.helper.ActivityHelper;
import com.de.rocket.helper.FragHelper;
import com.de.rocket.helper.LocaleHelper;
import com.de.rocket.helper.StatusBarHelper;
import com.de.rocket.ue.activity.RoActivity;
import com.de.rocket.helper.ViewInjectHelper;
import com.de.rocket.helper.RecordHelper;
import com.de.rocket.ue.widget.FragRouterWidget;
import com.de.rocket.utils.RoLogUtil;
import com.de.rocket.utils.WidgetUtil;

import java.util.Locale;

/**
 * 本类作用是暴露内部接口供外部使用
 * Created by haide.yin(haide.yin@tcl.com) on 2019/6/24 17:43.
 */
public class Rocket {

    /* ************************************************************* */
    /*                           国际化相关
    /* ************************************************************* */

    /**
     * 设置APP需要缓存的语言
     *
     * @param locale 语言
     */
    public static void setSaveLocale(Context context, Locale locale) {
        LocaleHelper.setLocale(context, locale);
    }

    /**
     * 读取APP需要缓存的语言
     *
     * @return locale
     */
    public static Locale getSaveLocale(Context context) {
        return LocaleHelper.getLocale(context);
    }

    /* ************************************************************* */
    /*                       View反射注解相关
    /* ************************************************************* */

    /**
     * 绑定ViewHolder,之后可以注解的方式获取绑定viewy以及事件注解.用法如下：
     * ViewHolder(View itemView) {
     *   super(itemView);
     *   Rocket.bindViewHolder(this,itemView);//View注解
     * }
     * @param viewHolder viewHolder
     * @param view itemView
     */
    public static void bindViewHolder(Object viewHolder, View view) {
        ViewInjectHelper.getInstance().injectViewHolder(viewHolder, view);
    }

    /**
     * 绑定View 主要用户绑定Widget
     *
     * @param view itemView
     */
    public static void bindView(View view) {
        ViewInjectHelper.getInstance().injectView(view);
    }

    /* ************************************************************* */
    /*                       Rocket日志相关
    /* ************************************************************* */

    /**
     * 开放指定类型的Log,默认全开
     *
     * @param logtype Log类型
     */
    public static void setLogType(RoLogUtil.LOGTYPE logtype) {
        RoLogUtil.openLogType(logtype);
    }

    /**
     * 写入Log信息
     *
     * @param logString Log信息
     */
    public static void writeOuterLog(String logString){
        Activity topActivity = getTopActivity();
        if(topActivity != null && !topActivity.isFinishing()){
            RecordHelper.writeOuterLog(topActivity,logString);
        }
    }

    /* ************************************************************* */
    /*           Application 、 Activity 、 Fragment 相关
    /* ************************************************************* */

    /**
     * 读取当前的Application
     *
     * @return  Application
     */
    public static Application getApplication() {
        return RoApplication.APPLICATION;
    }

    /**
     * 读取当前的栈顶的Activity
     *
     * @return  TOP_ACTIVITY
     */
    public static RoActivity getTopActivity() {
        return (RoActivity) ActivityHelper.getTopActivity();
    }

    /**
     * 全局显示悬浮球以及栈视图
     */
    public static void showGloaBall() {
        WidgetUtil.showGloaBall(topActivity -> {
            FragRouterWidget fragRouterWidget1 = new FragRouterWidget(topActivity);
            fragRouterWidget1.showStackView();
            return fragRouterWidget1;
        });
    }

    /* ************************************************************* */
    /*                          状态栏相关
    /* ************************************************************* */

    /**
     * 设置状态栏颜色
     *
     * @param color         状态栏颜色值
     * @param isImmersion   是否是沉浸式风格，隐藏状态栏创建自定义状态栏
     */
    public static void setStatusBarColor(Activity activity,@ColorInt int color, boolean isImmersion) {
        StatusBarHelper.setStatusBarColor(activity,color,isImmersion);
    }

    /**
     * 使指定的View向下Padding偏移一个状态栏高度，留出状态栏空间，主要用于设置沉浸式后,页面顶到顶端有突兀感
     *
     * @param targetView        需要偏移的View
     * @param enable            开启或者关闭
     * @param isPaddingOrMargin 向下偏移是padding还是margin，true的话是padding，false的话是margin
     */
    public static void setOffsetStatusBar(Activity activity,View targetView, boolean enable, boolean isPaddingOrMargin) {
        StatusBarHelper.setOffsetStatusBar(activity,targetView,enable,isPaddingOrMargin);
    }

    /* ************************************************************* */
    /*                           栈的操作
    /* ************************************************************* */

    /**
     * 读取当前的Fragment是否是处于栈顶,防止在后台跳转的情况
     *
     * @return boolean 是否处于栈顶
     */
    public static boolean isTopRocketStack(String fragmentTag) {
        RoActivity activity = getTopActivity();
        if(activity != null){
            return activity.getStack().isTopRocketStack(fragmentTag);
        }
        return false;
    }

    /**
     * 读取当前的Fragment
     */
    public static Fragment getTopRocketStackFragment(RoActivity activity){
        return FragHelper.getInstance().getTopRocketStackFragment(activity);
    }

    /* ************************************************************* */
    /*                          实用工具类
    /* ************************************************************* */

    /**
     * Android7.0以后，不同应用之间不允许传递Uri链接，有两种方式可以实现uri的传递
     * 绕过版本限制，删除Uri的检测
     */
    public static void detectFileUriExposure() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.detectFileUriExposure();
        }
    }

    /**
     * 显示状态栏,这句话配合APP启动配置的全屏window主题
     * <item name="android:windowFullscreen">true</item>
     */
    public static void clearWindowFullscreen(Activity activity){
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
