package com.de.rocket.ue.animation;

import java.io.Serializable;

/**
 * Fragment进场动画类，使用策略设计模式
 */
public abstract class FragAnimation implements Cloneable, Serializable {

    /**
     * 进场动画
     */
    public abstract int getEnter();

    /**
     * 出场动画
     */
    public abstract int getExit();

    /**
     * 默认动画效果
     */
    public int getTransitionType(){
        return 0;
    }
}
