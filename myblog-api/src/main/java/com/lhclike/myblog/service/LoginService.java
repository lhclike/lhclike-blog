package com.lhclike.myblog.service;

import com.lhclike.myblog.pojo.SysUser;
import com.lhclike.myblog.vo.LoginParam;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.params.LoginBySmsParam;
import com.lhclike.myblog.vo.params.LoginSendPhonenumber;

public interface LoginService {
    Result login(LoginParam loginParam);
    Result logout(String token);
    Result register(LoginParam loginParam);
    SysUser checkToken(String token);
    Result loginBySms(LoginBySmsParam loginBySmsParam);
    Result loginSendSms(LoginSendPhonenumber loginSendPhonenumber);
}
