package com.itheima.reggie.common;

public class BaseContext {

    //创建一个线程对象1
    private  static ThreadLocal<Long> THREAD_LOCA = new ThreadLocal<>();

    //设置值
    public static void set(Long employee){
        THREAD_LOCA.set(employee);
    }
    //获取值
    public static long get(){
        return THREAD_LOCA.get();
    }
    //提供静态方法移除数据
    public static void deleteCurrentId(){
        THREAD_LOCA.remove();
    }
}
