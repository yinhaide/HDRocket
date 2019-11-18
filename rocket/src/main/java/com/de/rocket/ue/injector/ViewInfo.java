package com.de.rocket.ue.injector;

/**
 * view的结构体
 * 如果想让这个类为内部类，其他地方不能引用的话用final修饰
 * final class ViewInfo{}
 */
public class ViewInfo {

    public int value;//本身的id
    public int parentId;//父类的id

    @Override
    public boolean equals(Object o) {//判断相等
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewInfo viewInfo = (ViewInfo) o;
        if (value != viewInfo.value) return false;
        return parentId == viewInfo.parentId;

    }

    @Override
    public int hashCode() {//输出hash码
        int result = value;
        result = 31 * result + parentId;
        return result;
    }
}
