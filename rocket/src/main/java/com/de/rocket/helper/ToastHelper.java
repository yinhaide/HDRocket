package com.de.rocket.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.de.rocket.R;
import com.de.rocket.Rocket;
import com.de.rocket.ue.widget.RoToast;
import com.de.rocket.utils.ExecutorUtil;
import com.de.rocket.utils.WidgetUtil;

import java.util.concurrent.ScheduledFuture;

/**
 * 吐司的工具类
 * Created by haide.yin(haide.yin@tcl.com) on 2019/6/21 13:24.
 */
public class ToastHelper {

    private static final int TOAST_WIDGET_ID = R.id.rocket_toast_view;//用户缓存的ViewID
    private static final int FADE_DURATION = 800;//渐变显示与渐变隐藏的时间间隔
    public static int DEFAULT_MILTIME = 3000;//默认提示时间
    public static int DEFAULT_GRAVATY = 1001;//默认的位置
    public static int DEFAULT_XOFFSET = 0;//默认水平偏移
    public static int DEFAULT_YOFFSET = 0;//默认竖直偏移
    private static Toast toast;//全局的吐司
    //缓存延迟删除Toast的对象
    private static ScheduledFuture scheduledFuture;

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
        //取消延迟隐藏View
        if(scheduledFuture != null){
            scheduledFuture.cancel(true);
        }
        if (toast != null) {
            toast.setText(tip);
        } else {
            toast = Toast.makeText(context, tip, Toast.LENGTH_LONG);
        }
        if (gravity != DEFAULT_GRAVATY) {
            toast.setGravity(gravity, xOffset, yOffset);
        }
        toast.show();
        //延迟隐藏吐司
        scheduledFuture = ExecutorUtil.get().sheduleDelay(duration, () -> ExecutorUtil.get().postMainLooper(() -> {
            if (toast != null) {
                toast.cancel();
                toast = null;//置空防止内存泄漏
            }
        }));
    }

    /**
     * 渐变显示
     *
     * @param tips     提示语
     * @param duration 显示时间间隔
     */
    public static void toastCustom(String tips, int duration) {
        Activity topActivity = Rocket.getTopActivity();
        if (topActivity != null) {
            //取消延迟隐藏View
            if(scheduledFuture != null){
                scheduledFuture.cancel(true);
            }
            RoToast toastView = new RoToast(topActivity);
            toastView.setText(tips);
            WidgetUtil.showGloaBal(topActivity, toastView, TOAST_WIDGET_ID, FADE_DURATION, view -> {
                ((RoToast)view).setText(tips);
                return true;
            });
            //延迟隐藏吐司
            scheduledFuture = ExecutorUtil.get().sheduleDelay(duration, () -> ExecutorUtil.get().postMainLooper(() -> {
                WidgetUtil.hideGloBal(topActivity,TOAST_WIDGET_ID,FADE_DURATION);
            }));
        }
    }
}
