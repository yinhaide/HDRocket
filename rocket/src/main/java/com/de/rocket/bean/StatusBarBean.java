package com.de.rocket.bean;

import android.support.annotation.ColorInt;

import com.de.rocket.R;

public class StatusBarBean extends RoBean {
    private boolean immersion;//是否直接隐藏掉状态栏，true:隐藏且不占空间,主要做沉浸风格 false:占据固定状态栏高度
    private @ColorInt int color;//状态栏的颜色,包括透明度在里面

    public StatusBarBean(){
        //默认值
        this.immersion = true;
        this.color = R.color.rocket_cl_primary;
    }

    public StatusBarBean(boolean immersion,int color) {
        this.immersion = immersion;
        this.color = color;
    }

    public boolean isImmersion() {
        return immersion;
    }

    public void setImmersion(boolean immersion) {
        this.immersion = immersion;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "StatusBarBean{" +
                "immersion=" + immersion +
                ", color=" + color +
                '}';
    }
}
