package com.lhclike.myblog.vo.params;

import lombok.Data;

@Data
public class LoginBySmsParam {

    private String phoneNumber;
    //验证码
    private String code;

    public LoginBySmsParam(String phonenumber, String code) {
        this.phoneNumber = phonenumber;
        this.code = code;
    }

    public LoginBySmsParam() {
    }

    public LoginBySmsParam(String phonenumber) {
        this.phoneNumber = phonenumber;
    }
}
