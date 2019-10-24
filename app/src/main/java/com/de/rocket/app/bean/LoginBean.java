package com.de.rocket.app.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 类作用描述
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/10 8:24.
 */
public class LoginBean implements Serializable {
    private String access_token; //token
    private String uid;//用户的uid
    private String platform;//登录平台，第三方登录要用到，如“twitter”，“facebook”
    private long expired_at;//token过期时间
    private long account_expired_time;//账号失效时间

    public LoginBean() {

    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpired_at() {
        return this.expired_at;
    }

    public void setExpired_at(long expired_at) {
        this.expired_at = expired_at;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public long getAccount_expired_time() {
        return account_expired_time;
    }

    public void setAccount_expired_time(long account_expired_time) {
        this.account_expired_time = account_expired_time;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "access_token='" + access_token + '\'' +
                ", uid='" + uid + '\'' +
                ", platform='" + platform + '\'' +
                ", expired_at=" + expired_at +
                ", account_expired_time=" + account_expired_time +
                '}';
    }

    public void copy(LoginBean bean) {
        this.platform = !TextUtils.isEmpty(bean.getPlatform()) ? bean.getPlatform() : this.platform;
        this.uid = bean.getUid();
        this.access_token = bean.getAccess_token();
        this.expired_at = bean.getExpired_at();
    }
}
