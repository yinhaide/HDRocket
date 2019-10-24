package com.de.rocket.app.bean;

/**
 * 类作用描述
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/10 15:20.
 */
public class ServerError {

    public int error_id;
    public String error_field;
    public String error_msg;

    public ServerError() {

    }

    public ServerError(int error_id, String error_field, String error_msg) {
        this.error_id = error_id;
        this.error_field = error_field;
        this.error_msg = error_msg;
    }

    public int getError_id() {
        return error_id;
    }

    public void setError_id(int error_id) {
        this.error_id = error_id;
    }

    public String getError_field() {
        return error_field;
    }

    public void setError_field(String error_field) {
        this.error_field = error_field;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    @Override
    public String toString() {
        return "ServerError{" +
                "error_id=" + error_id +
                ", error_field='" + error_field + '\'' +
                ", error_msg='" + error_msg + '\'' +
                '}';
    }
}
