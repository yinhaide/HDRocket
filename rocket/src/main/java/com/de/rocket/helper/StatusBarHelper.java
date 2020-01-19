package com.de.rocket.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.de.rocket.R;

/**
 * fitsSystemWindows：
 * 根据官方文档，如果某个View 的fitsSystemWindows 设为true，那么该View的padding属性将由系统设置，
 * 用户在布局文件中设置的padding会被忽略。系统会为该View设置一个paddingTop，值为statusbar的高度。fitsSystemWindows默认为false。
 * 只有将statusbar设为透明，或者界面设为全屏显示时，fitsSystemWindows才会起作用。不然statusbar的空间轮不到用户处理
 *
 * 状态栏的状态资料参考：
 * https://www.jianshu.com/p/e6656707f56c
 */
public class StatusBarHelper {

    //自定义状态栏对应的id
    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.rocket_statusbar_view;
    //ViewMargin偏移的tag
    private static final int TAG_KEY_HAVE_SET_MARGIN = -123;
    //ViewPadding偏移的tag
    private static final int TAG_KEY_HAVE_SET_PADDING = -124;
    //原始的状态栏颜色
    private static @ColorInt int oldStatusBarColor = Color.parseColor("#398EFF");

    /**
     * 设置状态栏颜色
     *
     * @param activity      需要设置的activity
     * @param color         状态栏颜色值
     * @param isImmersion   是否要沉浸风格，是的画将会隐藏状态栏然后创建自定义的状态栏
     */
    public static void setStatusBarColor(Activity activity, @ColorInt int color,boolean isImmersion) {
        if(isImmersion){
            setImmersionBarColor(activity,color);
        }else{
            setStatusBarColor(activity,color);
        }
    }

    /**
     * 使指定的View向下Padding偏移一个状态栏高度，留出状态栏空间，主要用于设置沉浸式后,页面顶到顶端有突兀感
     *
     * @param activity          需要设置的activity
     * @param targetView        需要偏移的View
     * @param enable            开启或者关闭
     * @param isPaddingOrMargin 向下偏移是padding还是margin，true的话是padding，false的话是margin
     */
    public static void setOffsetStatusBar(Activity activity, View targetView,boolean enable,boolean isPaddingOrMargin) {
        if(isPaddingOrMargin){
            setPaddingStatusBar(activity,targetView,enable);
        }else{
            setMarginStatusBar(activity,targetView,enable);
        }
    }

    /**
     * 设置沉浸式风格的状态栏目(取消状态栏的的占位符)
     *
     * @param activity fragment 对应的 activity
     * @param color    颜色
     */
    private static void setImmersionBarColor(Activity activity, @ColorInt int color) {
        //首先取消状态栏
        showStatusbarForWindow(activity,false);
        //4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
            //加入新的状态栏（不加入队列的话会出现页面偏移的Bug）
            new Handler().post(() -> addStatusBarView(activity, color));
        }
    }

    /**
     * 设置透明,隐藏状态栏
     *
     * @param activity 需要设置的 activity
     */
    private static void showStatusbarForWindow(Activity activity,boolean show) {
        if(show){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                activity.getWindow().setStatusBarColor(oldStatusBarColor);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                oldStatusBarColor = activity.getWindow().getStatusBarColor();
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /**
     * 使指定的View向下Padding偏移一个状态栏高度，留出状态栏空间，主要用于设置沉浸式后,页面顶到顶端有突兀感
     *
     * @param activity      需要设置的activity
     * @param targetView    需要偏移的View
     * @param enable        开启或者关闭
     */
    private static void setPaddingStatusBar(Activity activity, View targetView,boolean enable) {
        if (targetView != null) {
            Object haveSetOffset = targetView.getTag(TAG_KEY_HAVE_SET_PADDING);
            int paddinTop;
            if(enable){
                if (haveSetOffset != null && (Boolean) haveSetOffset) {
                    return;
                }
                paddinTop = targetView.getPaddingTop() + getStatusBarHeight(activity);
            }else{
                if (haveSetOffset != null && !(Boolean) haveSetOffset) {
                    return;
                }
                paddinTop = targetView.getPaddingTop() - getStatusBarHeight(activity);
            }
            targetView.setPadding(targetView.getPaddingLeft(),paddinTop,targetView.getPaddingRight(),targetView.getPaddingBottom());
            targetView.setTag(TAG_KEY_HAVE_SET_PADDING, enable);
        }
    }

    /**
     * 使指定的View向下Margin偏移一个状态栏高度，留出状态栏空间，主要用于设置沉浸式后,页面顶到顶端有突兀感
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要偏移的View
     */
    private static void setMarginStatusBar(Activity activity, View needOffsetView,boolean enable) {
        if (needOffsetView != null) {
            Object haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_MARGIN);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            int marginTop;
            if(enable){
                if (haveSetOffset != null && (Boolean) haveSetOffset) {
                    return;
                }
                marginTop = layoutParams.topMargin + getStatusBarHeight(activity);
            }else{
                if (haveSetOffset != null && !(Boolean) haveSetOffset) {
                    return;
                }
                marginTop = layoutParams.topMargin - getStatusBarHeight(activity);
            }
            layoutParams.setMargins(layoutParams.leftMargin, marginTop,layoutParams.rightMargin, layoutParams.bottomMargin);
            needOffsetView.setLayoutParams(layoutParams);
            needOffsetView.setTag(TAG_KEY_HAVE_SET_MARGIN, enable);
        }
    }


    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     */

    private static void setStatusBarColor(Activity activity, @ColorInt int color) {
        //显示原来的状态栏
        cancelImmersionStyle(activity);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View fakeStatusBarView = decorView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }
                fakeStatusBarView.setBackgroundColor(color);
            } else {
                decorView.addView(createStatusBarView(activity, color));
            }
            setRootView(activity);
        }
    }

    /**
     * 取消沉浸式风格的状态栏
     *
     * @param activity activity
     */
    private static void cancelImmersionStyle(Activity activity) {
        //显示原来的状态栏
        showStatusbarForWindow(activity,true);
        //隐藏自定义的状态
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.VISIBLE) {
                fakeTranslucentView.setVisibility(View.GONE);
            }
            decorView.removeView(fakeTranslucentView);
        }
    }

    /**
     * 设置根布局参数
     *
     * @param activity 需要设置的 activity
     */
    private static void setRootView(Activity activity) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    /**
     * 添加自定义状态栏条目
     *
     * @param activity 需要设置的 activity
     * @param color    颜色
     */
    private static void addStatusBarView(Activity activity, @ColorInt int color) {
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.GONE) {
                fakeTranslucentView.setVisibility(View.VISIBLE);
            }
            fakeTranslucentView.setBackgroundColor(color);
        } else {
            View statusBarView = createStatusBarView(activity, color);
            contentView.addView(statusBarView);
        }
    }

    /**
     * 创建自定义状态栏条目
     *
     * @param color 颜色
     * @return View
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return statusBarView;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 清除之前的设置
     *
     * @param activity 需要设置的 activity
     */
    private static void clearPreviousSetting(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeStatusBarView != null) {
            decorView.removeView(fakeStatusBarView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }
    }
}
