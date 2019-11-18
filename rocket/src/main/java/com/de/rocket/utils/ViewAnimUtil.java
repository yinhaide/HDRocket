package com.de.rocket.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 控制View的可视化帮助类,用法
 * <p>
 * ViewAnimUtil.showFade(fatherView, 0f,1f,100);
 * ViewAnimUtil.showFromBottom(childView,200);
 * <p>
 * ViewAnimUtil.hideToBottom(childView, 200);
 * ViewAnimUtil.hideFade(fatherView, 1f,0f,100);
 * <p>
 * Created by haide.yin(haide.yin@tcl.com) on 2019/2/25 10:20.
 */
public class ViewAnimUtil {

    /**
     * 从底下弹出
     *
     * @param view      the view
     * @param duraction 动画持续时间
     */
    public static void showFromBottom(View view, int duraction) {
        if (view != null && view.getVisibility() == View.GONE) {
            view.setEnabled(true);
            TranslateAnimation showAnim = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            showAnim.setDuration(duraction);
            view.startAnimation(showAnim);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 到底下消失
     *
     * @param view      the view
     * @param duraction 动画持续时间
     */
    public static void hideToBottom(View view, int duraction) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setEnabled(false);
            TranslateAnimation hideAnim = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);
            hideAnim.setDuration(duraction);
            view.startAnimation(hideAnim);
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 渐变显示
     *
     * @param view       the view
     * @param startAlpha 开始的渐变
     * @param endAlpha   结束的渐变
     * @param duraction  动画持续时间
     */
    public static void showFade(View view, float startAlpha, float endAlpha, int duraction) {
        if (view != null && view.getVisibility() == View.GONE) {
            view.setEnabled(true);
            Animation animation = new AlphaAnimation(startAlpha, endAlpha);
            animation.setDuration(duraction);
            view.startAnimation(animation);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 渐变消失
     *
     * @param view       the view
     * @param startAlpha 开始的渐变
     * @param endAlpha   结束的渐变
     * @param duraction  动画持续时间
     */
    public static void hideFade(View view, float startAlpha, float endAlpha, int duraction) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setEnabled(false);
            Animation animation = new AlphaAnimation(startAlpha, endAlpha);
            animation.setDuration(duraction);
            view.startAnimation(animation);
            view.setVisibility(View.GONE);
        }
    }
}
