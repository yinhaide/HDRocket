package com.de.rocket.app.helper;

import android.util.Log;

/**
 * 测试Helper
 * Created by haide.yin(haide.yin@tcl.com) on 2019/9/19 15:53.
 */
public class TestHelper {

    /**
     * 测试回调
     */
    public void test(){
        onTestNext();
    }

    /* ***************************** Test ***************************** */

    private OnTestListener onTestListener;

    // 接口类 -> OnTestListener
    public interface OnTestListener {
        void onTest();
    }

    // 对外暴露接口 -> setOnTestListener
    public void setOnTestListener(OnTestListener onTestListener) {
        this.onTestListener = onTestListener;
    }

    // 内部使用方法 -> TestNext
    private void onTestNext() {
        if (onTestListener != null) {
            onTestListener.onTest();
        }
    }
}
