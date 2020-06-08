package com.de.rocket.ue.animation;

import com.de.rocket.R;

/**
 * 一个Fragment从上面出来
 */
public class TopPushFragAnimation extends FragAnimation {

    @Override
    public int getEnter() {
        return R.anim.anim_y_100_to_0;
    }

    @Override
    public int getExit() {
        return R.anim.none;
    }
}
