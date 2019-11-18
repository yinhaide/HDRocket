package com.de.rocket.bean;

public class PermissionBean extends RoBean {

    private String name;//权限的名字
    private boolean granted;//允许权限
    private boolean dontAskAgain;//不再询问

    public PermissionBean() {

    }

    public PermissionBean(String name, boolean granted, boolean dontAskAgain) {
        this.name = name;
        this.granted = granted;
        this.dontAskAgain = dontAskAgain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public boolean isDontAskAgain() {
        return dontAskAgain;
    }

    public void setDontAskAgain(boolean dontAskAgain) {
        this.dontAskAgain = dontAskAgain;
    }

    @Override
    public String toString() {
        return "PermissionBean{" +
                "name='" + name + '\'' +
                ", granted=" + granted +
                ", dontAskAgain=" + dontAskAgain +
                '}';
    }
}
