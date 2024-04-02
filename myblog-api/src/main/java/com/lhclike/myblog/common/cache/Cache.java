package com.lhclike.myblog.common.cache;

import java.lang.annotation.*;

//用于注解
@Target({ElementType.METHOD})
//运行时通过反射获得
@Retention(RetentionPolicy.RUNTIME)
//这个注解包含在JavaDoc中，这样生成的JavaDoc时能够显示注解的信息
@Documented
public @interface Cache {

    //定义了存在的时间
    long expire() default 1 * 60 * 1000;
    //定义这个缓存的名称
    String name() default "";

}
