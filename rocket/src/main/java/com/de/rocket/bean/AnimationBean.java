package com.de.rocket.bean;

import android.support.v4.app.FragmentTransaction;

import com.de.rocket.R;

/**
 * 动画的Bean(只支持进场出场的动画，不支持进出栈动画,因为框架不会用进出栈的方式)
 * Created by haide.yin(haide.yin@tcl.com) on 2019/10/24 17:37.
 */
public class AnimationBean extends RoBean {

    private int enter;//进场动画
    private int exit;//出场动画
    private int transitionID;

    public AnimationBean(int enter, int exit) {
        this.enter = enter;
        this.exit = exit;
    }

    public int getEnter() {
        return enter;
    }

    public void setEnter(int enter) {
        this.enter = enter;
    }

    public int getExit() {
        return exit;
    }

    public void setExit(int exit) {
        this.exit = exit;
    }

    public int getTransitionID() {
        return transitionID;
    }

    public void setTransitionID(int transitionID) {
        this.transitionID = transitionID;
    }

    @Override
    public String toString() {
        return "AnimationBean{" +
                "enter=" + enter +
                ", exit=" + exit +
                '}';
    }

    public static AnimationBean createFade(){
        AnimationBean animationBean = new AnimationBean(0,0);
        animationBean.setTransitionID(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        return animationBean;
    }

    public static AnimationBean createOpen(){
        AnimationBean animationBean = new AnimationBean(0,0);
        animationBean.setTransitionID(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        return animationBean;
    }

    public static AnimationBean createClose(){
        AnimationBean animationBean = new AnimationBean(0,0);
        animationBean.setTransitionID(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        return animationBean;
    }

    public static AnimationBean createTopPush(){
        return new AnimationBean(R.anim.anim_y_100_to_0, R.anim.none);
    }

    public static AnimationBean createTopTogether(){
        return new AnimationBean(R.anim.anim_y_100_to_0, R.anim.anim_y_0_to_m100);
    }

    public static AnimationBean createBottomPush(){
        return new AnimationBean(R.anim.anim_y_m100_to_0, R.anim.none);
    }

    public static AnimationBean createBottomTogether(){
        return new AnimationBean(R.anim.anim_y_m100_to_0, R.anim.anim_y_0_to_100);
    }

    public static AnimationBean createRightPush(){
        return new AnimationBean(R.anim.anim_x_m100_to_0, R.anim.none);
    }

    public static AnimationBean createRightTogether(){
        return new AnimationBean(R.anim.anim_x_m100_to_0, R.anim.anim_x_0_to_100);
    }

    public static AnimationBean createLeftPush(){
        return new AnimationBean(R.anim.anim_x_100_to_0, R.anim.none);
    }

    public static AnimationBean createLeftTogether(){
        return new AnimationBean(R.anim.anim_x_100_to_0, R.anim.anim_x_0_to_m100);
    }
}
