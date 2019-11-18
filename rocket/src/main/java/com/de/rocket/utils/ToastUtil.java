package com.de.rocket.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.de.rocket.R;
import com.de.rocket.Rocket;
import com.de.rocket.ue.widget.RoToast;

/**
 * 吐司的工具类
 * Created by haide.yin(haide.yin@tcl.com) on 2019/6/21 13:24.
 */
public class ToastUtil {

    private static final int TOAST_WIDGET_ID = R.id.rocket_toast_view;//用户缓存的ViewID
    private static final int FADE_DURATION = 800;//渐变显示与渐变隐藏的时间间隔
    public static int DEFAULT_MILTIME = 3000;//默认提示时间
    public static int DEFAULT_GRAVATY = 1001;//默认的位置
    public static int DEFAULT_XOFFSET = 0;//默认水平偏移
    public static int DEFAULT_YOFFSET = 0;//默认竖直偏移
    private static Toast toast;//全局的吐司
    private static boolean isToastCustom;//记录吐司的类型
    /**
     * 定时关闭清除
     */
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Runnable runable = new Runnable() {
        public void run() {
            if (isToastCustom) {//传统吐司
                cancel();
                remove();
            } else {//系统吐司
                if (toast != null) {
                    toast.cancel();
                    toast = null;//置空防止内存泄漏
                }
            }
        }
    };

    /**
     * 用系统自带的吐司
     *
     * @param context  上下文
     * @param tip      提示语
     * @param duration 显示时间毫秒,最长3500毫秒
     * @param gravity  位置 -1为默认的位置
     */
    @SuppressLint("ShowToast")
    public static void toastSystem(Context context, String tip, int duration, int gravity, int xOffset, int yOffset) {
        isToastCustom = false;
        handler.removeCallbacks(runable);
        if (toast != null) {
            toast.setText(tip);
        } else {
            toast = Toast.makeText(context, tip, Toast.LENGTH_LONG);
        }
        if (gravity != DEFAULT_GRAVATY) {
            toast.setGravity(gravity, xOffset, yOffset);
        }
        toast.show();
        handler.postDelayed(runable, duration);//延迟关闭
    }

    /**
     * 渐变显示
     *
     * @param tips     提示语
     * @param duration 显示时间间隔
     */
    public static void toastCustom(String tips, int duration) {
        isToastCustom = true;
        handler.removeCallbacks(runable);
        Activity topActivity = Rocket.getTopActivity();
        if (topActivity != null) {
            ViewGroup decorView = (ViewGroup) topActivity.getWindow().getDecorView();
            RoToast toastView = decorView.findViewById(TOAST_WIDGET_ID);
            if (toastView != null) {
                toastView.setText(tips);
            } else {
                toastView = new RoToast(topActivity);
                toastView.setText(tips);
                ViewGroup contentView = topActivity.findViewById(android.R.id.content);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                toastView.setLayoutParams(params);
                toastView.setId(TOAST_WIDGET_ID);
                contentView.addView(toastView);
                toastView.setVisibility(View.GONE);
            }
            if (toastView.getVisibility() == View.GONE) {
                //渐变显示
                ViewAnimUtil.showFade(toastView, 0, 1, FADE_DURATION);
            }
        }
        handler.postDelayed(runable, duration);
    }

    /**
     * 渐变隐藏
     */
    private static void cancel() {
        Activity activity = Rocket.getTopActivity();
        if (activity != null) {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View toastView = decorView.findViewById(TOAST_WIDGET_ID);
            if (toastView != null) {
                //渐变显示
                ViewAnimUtil.hideFade(toastView, 1, 0, FADE_DURATION);
            }
        }
    }

    /**
     * 清除
     */
    private static void remove() {
        Activity activity = Rocket.getTopActivity();
        if (activity != null) {
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View toastView = decorView.findViewById(TOAST_WIDGET_ID);
            if (toastView != null) {
                contentView.removeView(toastView);
            }
        }
    }
}
