package com.de.rocket.ue.animation;

import com.de.rocket.R;

/**
 * 两个Fragment一起从上面出来
 */
public class TopBothFragAnimation extends FragAnimation {

    @Override
    public int getEnter() {
        return R.anim.anim_y_100_to_0;
    }

    @Override
    public int getExit() {
        return R.anim.anim_y_0_to_m100;
    }
}
