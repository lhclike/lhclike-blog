package com.lhclike.myblog.vo.params;

import lombok.Data;

@Data
public class LoginBySmsParam {

    private String phoneNumber;
    //验证码
    private String smsCode;

    public LoginBySmsParam(String phonenumber, String smscode) {
        this.phoneNumber = phonenumber;
        this.smsCode = smscode;
    }

    public LoginBySmsParam() {
    }

    public LoginBySmsParam(String phonenumber) {
        this.phoneNumber = phonenumber;
    }
}
