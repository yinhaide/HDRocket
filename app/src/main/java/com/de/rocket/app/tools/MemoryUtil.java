package com.de.rocket.app.tools;

import android.os.Debug;
import android.util.Log;

public class MemoryUtil {

    /**
     * 对比性能参数的时间以及内存
     **/
    public static void printMemoryMsg(String type){
        Log.e("MemoryUtil",type+"_currentTimeMillis:"+System.currentTimeMillis());
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        int totalPss = memoryInfo.getTotalPss();//返回的是KB
        Log.e("MemoryUtil",type+"_totalPss:"+totalPss);
    }
}
