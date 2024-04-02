package com.lhclike.myblog.service;

import com.lhclike.myblog.pojo.SysUser;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.UserVo;


public interface UserService {

    SysUser findUserById(Long userId);
    SysUser findUser(String account,String pwd);
    Result getUserInfoByToken(String token);
    SysUser findUserByAccount(String account);
    SysUser findUserByPhoneNumber(String phoneNumber);
    void save(SysUser sysUser);
    SysUser checkToken(String token);
    UserVo findUserVoById(Long id);
}