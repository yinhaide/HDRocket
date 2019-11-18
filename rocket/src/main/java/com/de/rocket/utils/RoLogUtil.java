package com.de.rocket.utils;

import android.util.Log;

import com.de.rocket.cons.RoKey;


/**
 * 统一封装的Log工具类
 */
public class RoLogUtil {

    private static String TAG = RoKey.TAG_ROCKET;
    private static LOGTYPE logtype = LOGTYPE.ALL;

    /**
     * 开放指定类型的Log
     *
     * @param logtype Log类型
     */
    public static void openLogType(LOGTYPE logtype) {
        RoLogUtil.logtype = logtype;
    }

    /**
     * 关闭所有的Log
     */
    public static void stopALLLog() {
        RoLogUtil.logtype = LOGTYPE.STOP;
    }

    /**
     * 开放所有的Log
     */
    public static void openAllLog() {
        RoLogUtil.logtype = LOGTYPE.ALL;
    }

    /**
     * Log.v 长条提示
     *
     * @param msg msg
     */
    public static void v(String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    /**
     * Log.v 长条提示
     *
     * @param tag tag
     * @param msg msg
     */
    public static void v(String tag, String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.VERBOSE) {
            Log.v(tag, msg);
        }
    }

    /**
     * Log.d 调试提示
     *
     * @param msg msg
     */
    public static void d(String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Log.d 调试提示
     *
     * @param tag tag
     * @param msg msg
     */
    public static void d(String tag, String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Log.i 信息提示
     *
     * @param msg msg
     */
    public static void i(String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.INFO) {
            Log.i(TAG, msg);
        }
    }

    /**
     * Log.i 信息提示
     *
     * @param tag tag
     * @param msg msg
     */
    public static void i(String tag, String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     * Log.w 警告提示
     *
     * @param msg msg
     */
    public static void w(String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.WARN) {
            Log.w(TAG, msg);
        }
    }

    /**
     * Log.w 警告提示
     *
     * @param tag tag
     * @param msg msg
     */
    public static void w(String tag, String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     * Log.e 错误提示
     *
     * @param msg msg
     */
    public static void e(String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.ERROR) {
            Log.e(TAG, msg);
        }
    }

    /**
     * Log.e 错误提示
     *
     * @param tag tag
     * @param msg msg
     */
    public static void e(String tag, String msg) {
        if (logtype == LOGTYPE.ALL || logtype == LOGTYPE.ERROR) {
            Log.e(tag, msg);
        }
    }

    /**
     * 开放Log类型枚举
     */
    public enum LOGTYPE {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ALL, STOP
    }
}