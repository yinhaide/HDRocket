package com.de.rocket.ue.animation;

import com.de.rocket.R;

/**
 * 两个Fragment一起从右边出来
 */
public class RightBothFragAnimation extends FragAnimation {

    @Override
    public int getEnter() {
        return R.anim.anim_x_m100_to_0;
    }

    @Override
    public int getExit() {
        return R.anim.anim_x_0_to_100;
    }
}
