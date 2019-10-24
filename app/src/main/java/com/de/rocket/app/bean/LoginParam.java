package com.de.rocket.app.bean;

import com.de.rocket.app.BuildConfig;

import java.io.Serializable;

public class LoginParam implements Serializable {

    private String username;//邮箱
    private String password;//密码
    private String app_version;//app版本号
    private String devicename;//设备名
    private long devicetime;//登陆时间

    public LoginParam() {
    }

    public LoginParam(String username, String password) {
        this.username = username;
        this.password = password;
        this.app_version = String.valueOf(BuildConfig.VERSION_CODE);
        this.devicename = android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL;
        this.devicetime = System.currentTimeMillis() / 1000;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public long getDevicetime() {
        return devicetime;
    }

    public void setDevicetime(long devicetime) {
        this.devicetime = devicetime;
    }
}
