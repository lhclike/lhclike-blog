package com.lhclike.myblog;

import com.alibaba.fastjson.annotation.JSONField;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.lhclike.myblog.admin.mapper")
public class AdminApp {

    public static void main(String[] args) {
        SpringApplication.run(AdminApp.class,args);
    }
}
