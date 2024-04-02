package com.lhclike.myblog.vo;

import lombok.Data;

@Data
public class LoginUserVo {
    private Long id;
    private String account;
    private String nickaname;
    private String avatar;
    private String phoneNumber;
}
