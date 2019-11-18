package com.de.rocket.bean;

/**
 * 记录日志的Bean
 * 日志路径：mnt/sdcard/Android/data/<package name>/files/rocket/<date>/xx
 * Created by haide.yin(haide.yin@tcl.com) on 2019/10/14 10:07.
 */
public class RecordBean extends RoBean {

    //是否允许记录崩溃日志(mnt/sdcard/Android/data/<package name>/files/rocket/<date>/crash.log)
    private boolean crashEnable;
    //是否允许记录崩溃日志(mnt/sdcard/Android/data/<package name>/files/rocket/<date>/inner.log)
    private boolean innerEnable;
    //是否允许记录崩溃日志(mnt/sdcard/Android/data/<package name>/files/rocket/<date>/outer.log)
    private boolean outerEnable;
    //日志保存的时间时长，超过自动删除
    private int saveDay;

    public RecordBean() {//默认值
        this.crashEnable = true;
        this.innerEnable = true;
        this.outerEnable = true;
        this.saveDay = 7;
    }

    public RecordBean(boolean crashEnable, boolean innerEnable, boolean outerEnable, int saveDay) {
        this.crashEnable = crashEnable;
        this.innerEnable = innerEnable;
        this.outerEnable = outerEnable;
        this.saveDay = saveDay;
    }

    public boolean isCrashEnable() {
        return crashEnable;
    }

    public void setCrashEnable(boolean crashEnable) {
        this.crashEnable = crashEnable;
    }

    public boolean isInnerEnable() {
        return innerEnable;
    }

    public void setInnerEnable(boolean innerEnable) {
        this.innerEnable = innerEnable;
    }

    public boolean isOuterEnable() {
        return outerEnable;
    }

    public void setOuterEnable(boolean outerEnable) {
        this.outerEnable = outerEnable;
    }

    public int getSaveDay() {
        return saveDay;
    }

    public void setSaveDay(int saveDay) {
        this.saveDay = saveDay;
    }

    @Override
    public String toString() {
        return "RecordBean{" +
                "crashEnable=" + crashEnable +
                ", innerEnable=" + innerEnable +
                ", outerEnable=" + outerEnable +
                ", saveDay=" + saveDay +
                '}';
    }
}
