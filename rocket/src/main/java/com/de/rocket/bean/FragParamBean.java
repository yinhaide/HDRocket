package com.de.rocket.bean;

import com.de.rocket.ue.animation.FragAnimation;

/**
 * Fragment页面切换参数集合，采用Builder设计模式
 * Created by haide.yin(haide.yin@tcl.com) on 2020/6/7 10:07.
 */
public class FragParamBean extends RoBean {

    //是否从内存中移除当前的Fragment
    private boolean isOriginalRemove;
    //是否重新创建目标Fragment
    private boolean isTargetReload;
    //页面之间传递的对象
    private Object translateObject;
    //是否从内存中清除目标Fragment在路由栈上面的Fragment
    private boolean isClearTop;
    //页面转场动画
    private FragAnimation fragAnimation;

    /**
     * 外部类提供一个私有构造函数供内部类调用，在该构造函数中完成成员变量的赋值，取值为Builder对象中对应的成变量的值。
     */
    private FragParamBean(Builder builder) {
        this.isOriginalRemove = builder.isOriginalRemove;
        this.isTargetReload = builder.isTargetReload;
        this.translateObject = builder.translateObject;
        this.isClearTop = builder.isClearTop;
        this.fragAnimation = builder.fragAnimation;
    }

    public boolean isOriginalRemove() {
        return isOriginalRemove;
    }

    public void setOriginalRemove(boolean originalRemove) {
        isOriginalRemove = originalRemove;
    }

    public boolean isTargetReload() {
        return isTargetReload;
    }

    public void setTargetReload(boolean targetReload) {
        isTargetReload = targetReload;
    }

    public Object getTranslateObject() {
        return translateObject;
    }

    public void setTranslateObject(Object translateObject) {
        this.translateObject = translateObject;
    }

    public boolean isClearTop() {
        return isClearTop;
    }

    public void setClearTop(boolean clearTop) {
        isClearTop = clearTop;
    }

    public FragAnimation getFragAnimation() {
        return fragAnimation;
    }

    public void setFragAnimation(FragAnimation fragAnimation) {
        this.fragAnimation = fragAnimation;
    }

    /**
     * 定义一个静态内部类Builder，内部的成员变量和外部类一样
     */
    public static class Builder {

        //是否从内存中移除当前的Fragment
        private boolean isOriginalRemove;
        //是否重新创建目标Fragment
        private boolean isTargetReload;
        //页面之间传递的对象
        private Object translateObject;
        //是否从内存中清除目标Fragment在路由栈上面的Fragment
        private boolean isClearTop;
        //页面转场动画
        private FragAnimation fragAnimation;

        /**
         * Builder类通过一系列的方法用于成员变量的赋值，并返回当前对象本身（this）
         */
        public  Builder isOriginalRemove(boolean isOriginalRemove){
            this.isOriginalRemove = isOriginalRemove;
            return  this;
        }

        /**
         * Builder类通过一系列的方法用于成员变量的赋值，并返回当前对象本身（this）
         */
        public  Builder isTargetReload(boolean isTargetReload){
            this.isTargetReload = isTargetReload;
            return  this;
        }

        /**
         * Builder类通过一系列的方法用于成员变量的赋值，并返回当前对象本身（this）
         */
        public  Builder translateObject(Object translateObject){
            this.translateObject = translateObject;
            return  this;
        }

        /**
         * Builder类通过一系列的方法用于成员变量的赋值，并返回当前对象本身（this）
         */
        public  Builder isClearTop(boolean isClearTop){
            this.isClearTop = isClearTop;
            return  this;
        }

        /**
         * Builder类通过一系列的方法用于成员变量的赋值，并返回当前对象本身（this）
         */
        public  Builder fragAnimation(FragAnimation fragAnimation){
            this.fragAnimation = fragAnimation;
            return  this;
        }

        /**
         * Builder类提供一个外部类的创建方法（build、create），该方法内部调用了外部类的一个私有构造函数，入参就是内部类Builder
         */
        public FragParamBean build() {//build或者create构建对象
            return new FragParamBean(this);
        }
    }
}
