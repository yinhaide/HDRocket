package com.de.rocket.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 统一封装的SharePreference工具类
 */
public class SharePreUtil {

    private static String SP_NAME = "SPU";
    private static int MODE = 0;
    private static volatile SharePreUtil sharedPrefsUtil;

    private SharePreUtil() {}

    public static SharePreUtil getInstance() {
        if (sharedPrefsUtil == null) {
            synchronized (SharePreUtil.class) {
                if (sharedPrefsUtil == null) {
                    sharedPrefsUtil = new SharePreUtil();
                }
            }
        }
        return sharedPrefsUtil;
    }

    /**
     * 得到存储文件名
     *
     * @return 文件名
     */
    public static String getSP_NAME() {
        return SP_NAME;
    }

    /**
     * 设置存储文件名
     *
     * @param sharedPrefsName 文件名
     * @return 单利
     */
    public SharePreUtil setSP_NAME(String sharedPrefsName) {
        SP_NAME = sharedPrefsName;
        return this;
    }

    /**
     * 得到进程的ApplicationContext，防止attachBaseContext的时候context.getApplicationContext为null
     *
     * @return ApplicationContext
     */
    private Context getApplicationContext(Context context) {
        if (context != null) {
            Context finalContext = context.getApplicationContext();
            if (finalContext != null) {
                return finalContext;
            }
        }
        return context;
    }

    /**
     * 存储整数
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public void putInt(Context context, String key, int value) {
        SharedPreferences.Editor sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE).edit();
        sp.putInt(key, value);
        sp.apply();
    }

    /**
     * 存储浮点数
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public void putFloat(Context context, String key, float value) {
        SharedPreferences.Editor sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE).edit();
        sp.putFloat(key, value);
        sp.apply();
    }

    /**
     * 存储长整数
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public void putLong(Context context, String key, long value) {
        SharedPreferences.Editor sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE).edit();
        sp.putLong(key, value);
        sp.apply();
    }

    /**
     * 存储布尔值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE).edit();
        sp.putBoolean(key, value);
        sp.apply();
    }

    /**
     * 存储字符串
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public void putString(Context context, String key, String value) {
        SharedPreferences.Editor sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE).edit();
        sp.putString(key, value);
        sp.apply();
    }

    /**
     * 取出指定键的整数
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回指定类型指定键的值
     */
    public int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE);
        return sp.getInt(key, defValue);
    }

    /**
     * 取出指定键的浮点数
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回指定类型指定键的值
     */
    public float getFloat(Context context, String key, float defValue) {
        SharedPreferences sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE);
        return sp.getFloat(key, defValue);
    }

    /**
     * 取出指定键的长整数
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回指定类型指定键的值
     */
    public long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE);
        return sp.getLong(key, defValue);
    }

    /**
     * 取出指定键的布尔值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回指定类型指定键的值
     */
    public boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 取出指定键的字符串
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回指定类型指定键的值
     */
    public String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE);
        return sp.getString(key, defValue);
    }

    /**
     * 时候存在某个键
     *
     * @param context 上下文
     * @param key     键
     * @return 是否存在
     */
    public boolean isExist(Context context, String key) {
        SharedPreferences sp = getApplicationContext(context).getSharedPreferences(SP_NAME, MODE);
        return sp.contains(key);
    }
}