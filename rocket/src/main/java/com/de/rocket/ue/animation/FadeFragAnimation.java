package com.de.rocket.ue.animation;

import android.support.v4.app.FragmentTransaction;

/**
 * 渐变动画
 */
public class FadeFragAnimation extends FragAnimation {

    @Override
    public int getEnter() {
        return 0;
    }

    @Override
    public int getExit() {
        return 0;
    }

    @Override
    public int getTransitionType() {
        return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
    }
}
