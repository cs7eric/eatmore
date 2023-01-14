package com.cs7eric.eatmore.util;


/**
 *  基于 ThreadLocal 封装工具类，用户保存和获取 用户id
 *
 * @author cs7eric
 * @date 2023/01/14
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void  setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
