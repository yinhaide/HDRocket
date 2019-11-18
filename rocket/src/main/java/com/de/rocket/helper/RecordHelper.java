package com.de.rocket.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.de.rocket.bean.RecordBean;
import com.de.rocket.utils.RoLogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 写日志的帮助类
 * Created by haide.yin(haide.yin@tcl.com) on 2019/9/6 16:11.
 */
public class RecordHelper {

    private static final String ROCKET = "rocket";//日志的文件夹mnt/sdcard/Android/data/<package name>/files/rocket
    private static final String OUTER_NAME = "outer.log";//外部日志的名字
    private static final String INNER_NAME = "inner.log";//内部日志的名字
    private static final String CRASH_NAME = "crash.log";//崩溃日志的名字
    private static RecordBean recordBean = new RecordBean();//日志的配置

    /**
     * 设置日志的配置
     */
    public static void setRecordBean(Context context,RecordBean recordBean) {
        RecordHelper.recordBean = recordBean;
        //删除超时的日志
        deleteTimeoutLog(context,recordBean.getSaveDay());
    }

    /**
     * 写入内部日志
     */
    public static void writeInnerLog(Context context,String content){
        if(recordBean.isInnerEnable()){
            writeLog(context,content,INNER_NAME);
        }
    }

    /**
     * 写外部日志
     */
    public static void writeOuterLog(Context context,String content){
        if(recordBean.isOuterEnable()){
            writeLog(context,content,OUTER_NAME);
        }
    }

    /**
     * 写崩溃日志
     */
    public static void writeCrashLog(Context context,String content){
        if(recordBean.isCrashEnable()){
            writeLog(context,content,CRASH_NAME);
        }
    }

    /**
     * 写入日志文件
     * 这类文件不应该存在SD卡的根目录下，而应该存在mnt/sdcard/Android/data/< package name >/files/…这个目录下。这类文件应该随着App的删除而一起删除
     * 需要权限
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     */
    @SuppressLint("SimpleDateFormat")
    private static void writeLog(Context context,String content,String filename){
        //SD卡是否已装入
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //当天的日期
            String dateFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //日志路径
            File logFilesDir = context.getExternalFilesDir(ROCKET);
            if(logFilesDir != null){
                File folderFile = new File(logFilesDir.getAbsolutePath() + File.separator+dateFolder);
                if(!folderFile.exists()){
                    folderFile.mkdir();
                }
                File logFile = new File(folderFile.getAbsolutePath()+File.separator+filename);
                if(!logFile.exists()){
                    try {
                        logFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(logFile.exists()){//文件不存在就创建
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    content = "\n" + dateStr + "--" + content;
                    try {
                        FileOutputStream output = new FileOutputStream(logFile, true);
                        byte[] desBytes = content.getBytes(StandardCharsets.UTF_8);
                        output.write(desBytes);
                        output.flush();
                        output.close();
                        RoLogUtil.v("RecordHelper::writeLog-->成功写入日志:"+content +" filename:"+filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                        RoLogUtil.e("RecordHelper::writeLog-->写入日志出错:"+e.toString());
                    }
                }
            }
        }
    }

    /**
     * 删除超过指定日期的日志
     * @param context 上下文
     */
    @SuppressLint("SimpleDateFormat")
    private static void deleteTimeoutLog(Context context,int saveDay){
        //SD卡是否已装入
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //日志路径
            File logFilesDir = context.getExternalFilesDir(ROCKET);
            if(logFilesDir != null){
                //找出目录下所有的文件夹
                File[] files = logFilesDir.listFiles();
                if(files != null && files.length > 0){
                    for(File file : files){
                        if(file.isDirectory()){//只删除文件夹
                            String folderName = file.getName();
                            try {
                                // 用parse方法，可能会异常，所以要try-catch
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(folderName);
                                if(date != null){
                                    //文件超过指定的日期就删除
                                    if(System.currentTimeMillis() - date.getTime() > saveDay * 24 * 3600 *1000){
                                        deleteFolderAndFile(file);
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除整个文件(包括文件夹以及文件)
     * @param file 文件对象
     */
    private static void deleteFolderAndFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFolderAndFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }
}
