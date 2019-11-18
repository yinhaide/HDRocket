package com.de.rocket.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.de.rocket.utils.RoLogUtil;
import com.de.rocket.utils.SharePreUtil;

import java.util.Locale;

/**
 * 多国语言适配
 * 使用方法
 * （1）Application 重写attachBaseContext()，传入 super.attachBaseContext(LocaleHelper.getContext(context));;
 * （2）Application 重写onConfigurationChanged()，传入 LocaleHelper.init(context);
 * （3）Application 重写onCreate()，传入 LocaleHelper.init(context);
 * （4）Activity 重写attachBaseContext()，传入 super.attachBaseContext(LocaleHelper.getContext(context));
 * （5）Service 重写attachBaseContext()，传入 super.attachBaseContext(LocaleHelper.getContext(context));
 * （6）LocaleHelper.setLocale(Context context, Locale locale);//在需要切换语言的地方保存语言
 * （7）MainActivity.reStart(this);//重启APP到主页面,可选
 * Created by haide.yin(haide.yin@tcl.com) on 2019/3/26 14:32.
 */
public class LocaleHelper {

    private static final String LANGUAGE_KEY = "LANGUAGE";
    private static final String COUNTRY_KEY = "COUNTRY";
    private static final String ISFROMEAPP_KEY = "IS_FROM_APP";

    /**
     * 初始化，设置上次选择的语言
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Locale locale = getLocale(context);
        String lang = locale.getLanguage();
        String cn = locale.getCountry();
        SharePreUtil.getInstance().putString(context, LANGUAGE_KEY, lang);
        SharePreUtil.getInstance().putString(context, COUNTRY_KEY, cn);
        updateConfiguration(context, lang, cn);
        if (context instanceof Activity) {
            updateConfiguration(context.getApplicationContext(), lang, cn);
        }
    }

    /**
     * 更新上下文语言配置
     *
     * @param context 上下文
     * @return 新的上下文
     */
    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            Locale locale = getLocale(context);
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            return context.createConfigurationContext(configuration);
        } else {
            return context;
        }
    }

    /**
     * 切换指定的语言
     *
     * @param context 上下文
     * @param locale  语言Locale
     */
    public static void setLocale(Context context, @NonNull Locale locale) {
        RoLogUtil.v("LocaleHelper::setLocale-->"+"locale:"+locale.toString());
        String cn = locale.getCountry();
        String lang = locale.getLanguage();
        SharePreUtil.getInstance().putString(context, LANGUAGE_KEY, lang);
        SharePreUtil.getInstance().putString(context, COUNTRY_KEY, cn == null ? "" : cn);
        SharePreUtil.getInstance().putBoolean(context, ISFROMEAPP_KEY, true);
        updateConfiguration(context, lang, cn);
        if (context instanceof Activity) {
            updateConfiguration(context.getApplicationContext(), lang, cn);
        }
    }

    /**
     * 得到本地语言，APP存储的优先，其次是系统选择的语言
     *
     * @param context 上下文
     * @return 当前语言
     */
    public static Locale getLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= 24) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        RoLogUtil.v("LocaleHelper::getLocale-->"+"locale:"+locale.toString());
        boolean isAppLang = SharePreUtil.getInstance().getBoolean(context, ISFROMEAPP_KEY, false);
        if (isAppLang) {
            String lang = SharePreUtil.getInstance().getString(context, LANGUAGE_KEY, locale.getLanguage());
            String cn = SharePreUtil.getInstance().getString(context, COUNTRY_KEY, locale.getCountry());
            if (TextUtils.isEmpty(cn)) {
                locale = new Locale(lang);
            } else {
                locale = new Locale(lang, cn.toUpperCase());
            }
            return locale;
        } else {
            return locale;
        }
    }

    /**
     * 设置新的语言，更新配置信息
     *
     * @param context 上下文
     * @param lang    语言Locale.getlanguage()
     * @param cn      国家Locale.getCountry()
     */
    @SuppressLint({"ObsoleteSdkInt"})
    private static void updateConfiguration(Context context, String lang, String cn) {
        Locale locale;
        if (TextUtils.isEmpty(cn)) {
            locale = new Locale(lang);
        } else {
            locale = new Locale(lang, cn.toUpperCase());
        }
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }
}
