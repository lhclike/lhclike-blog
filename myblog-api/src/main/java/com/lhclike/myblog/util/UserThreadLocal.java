package com.lhclike.myblog.util;

import com.lhclike.myblog.pojo.SysUser;

public class UserThreadLocal {
    private UserThreadLocal(){}

    private static final  ThreadLocal<SysUser> LOCAL=new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
