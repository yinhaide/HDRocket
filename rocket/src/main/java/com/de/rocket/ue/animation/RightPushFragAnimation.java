package com.de.rocket.ue.animation;

import com.de.rocket.R;

/**
 * 一个Fragment从右边出来
 */
public class RightPushFragAnimation extends FragAnimation {

    @Override
    public int getEnter() {
        return R.anim.anim_x_m100_to_0;
    }

    @Override
    public int getExit() {
        return R.anim.none;
    }
}
