package com.chuanwise.xiaoming.bot.nwu.sheet.data;

public class Student {
    long code;
    long qq;
    String name;
    String clazz;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getQq() {
        return qq;
    }

    public void setQq(long qq) {
        this.qq = qq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "Student{" +
                "code=" + code +
                ", qq=" + qq +
                ", name='" + name + '\'' +
                ", clazz='" + clazz + '\'' +
                '}';
    }
}