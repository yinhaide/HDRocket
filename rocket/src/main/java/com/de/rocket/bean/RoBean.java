package com.de.rocket.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class RoBean implements Cloneable, Serializable {

    /**
     * 深克隆,流复制的方式,防止对象地址一样，一动动全身
     *
     * @param <T> the type parameter
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public <T> T deepClone() {
        T stu = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bo);
            oos.writeObject(this);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            stu = (T) oi.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stu;
    }
}
