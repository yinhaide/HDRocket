package com.de.rocket.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.de.rocket.ue.activity.CrashActivity;
import com.de.rocket.utils.RoLogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * 崩溃处理
 */
public class CrashHelper implements Thread.UncaughtExceptionHandler{

    @SuppressLint("StaticFieldLeak")
    private static volatile CrashHelper instance;//单例
    private Thread.UncaughtExceptionHandler mDefaultHandler;//系统提供的处理异常方法
    private Context context;//存的是Application上下文,不会造成内存泄漏
    private boolean enableCrashWindow;//是否要隐藏自定义的崩溃的窗口

    /**
     * 单例
     */
    public static CrashHelper getInstance() {
        if (instance == null) {
            synchronized (CrashHelper.class) {
                if (instance == null) {
                    instance = new CrashHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     * @param context 上下文
     */
    public void initCrash(Context context){
        this.context = context.getApplicationContext();
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 是否弹出崩溃的窗口
     * @param enableCrashWindow 是否弹出崩溃的窗口
     */
    public void setEnableCrashWindow(boolean enableCrashWindow) {
        this.enableCrashWindow = enableCrashWindow;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if(e != null){
            //App处理
            String errTrace = getExceptionTrace(e);
            //打印输出
            RoLogUtil.e("CrashHelper::uncaughtException-->"+errTrace);
            //记录日志信息
            RecordHelper.writeCrashLog(context,errTrace);
            //显示异常的窗体(必须在页面正常起来之后才行，不然的话会无限重启)
            if(ActivityHelper.getTopActivity() != null){
                if(enableCrashWindow){
                    showCrashWindow(ActivityHelper.getTopActivity(),errTrace);
                }else{
                    systemException(t,e);
                }
            }else{
                systemException(t,e);
            }
        }else{
            systemException(t,null);
        }
    }

    /**
     * 交给系统内部处理异常
     */
    private void systemException(Thread t, Throwable e){
        if(mDefaultHandler != null){
            mDefaultHandler.uncaughtException(t, e);
        }
    }

    /**
     * 读取异常消息
     * @param ex 异常
     */
    private String getExceptionTrace(Throwable ex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ex.printStackTrace(ps);
        String errTrace = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        try {
            baos.close();
        } catch (IOException error) {
            //打印
            RoLogUtil.e("CrashHelper::getExceptionTrace-->"+error.toString());
            //记录日志信息
            RecordHelper.writeCrashLog(context,"CrashHelper::getExceptionTrace-->"+error.toString());
        }
        return errTrace;
    }

    /**
     * 展示异常信息
     * @param des 异常信息
     */
    private void showCrashWindow(Activity activity, String des) {
        Intent intent = new Intent(activity, CrashActivity.class);
        intent.putExtra(CrashActivity.ERROR_INTENT, des);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        //淡入淡出切换页面
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        activity.finish();
        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}