package com.lhclike.myblog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;



@Data
public class SysUser {
 //指定自动增长策略，根据数据库原则
   @TableId(value = "id",type= IdType.AUTO)
    private Long id;

    private String account;

    private Integer admin;

    private String avatar;

    private Long createDate;

    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String phoneNumber;

    private String nickname;

    private String password;

    private String salt;

    private String status;
}