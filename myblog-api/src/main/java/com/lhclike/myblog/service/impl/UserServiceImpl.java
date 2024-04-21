package com.lhclike.myblog.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.lhclike.myblog.util.JWTUtils;

import com.lhclike.myblog.dao.SysUserMapper;
import com.lhclike.myblog.pojo.SysUser;
import com.lhclike.myblog.service.UserService;
import com.lhclike.myblog.vo.ErrorCode;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.LoginUserVo;
import com.lhclike.myblog.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SysUser findUserById(Long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("lhclike");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String pwd) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,pwd);
        queryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getNickname);
        queryWrapper.last("limit 1");
        SysUser sysUser=sysUserMapper.selectOne(queryWrapper);
        return  sysUser;
    }

    @Override
    public Result getUserInfoByToken(String token) {
        Map<String,Object> map= JWTUtils.checkToken(token);
        System.out.println(map);
        if(map==null){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }
        String userJson= redisTemplate.opsForValue().get("TOKEN_"+token);
        System.out.println(userJson);
        if(userJson==null){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }
        String parseUserJson = JSON.parse(userJson).toString();
        SysUser sysUser= JSON.parseObject(parseUserJson,SysUser.class);
        LoginUserVo loginUserVo=new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickaname(sysUser.getNickname());
        loginUserVo.setPhoneNumber(sysUser.getPhoneNumber());
        return Result.success(loginUserVo);

    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }
    @Override
    public SysUser findUserByPhoneNumber(String phoneNumber) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getPhoneNumber,phoneNumber);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }


    @Override
    public void save(SysUser sysUser) {
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public SysUser checkToken(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String,Object> stringObjectMap=JWTUtils.checkToken(token);
        if(stringObjectMap==null){
            return null;
        }
        String userJson=redisTemplate.opsForValue().get("TOKEN_"+token);
        if(StringUtils.isBlank(userJson)){
            return  null;
        }
        System.out.println("-----------------------------------------------------------------------");
        //userJson中有很对斜线
        System.out.println(userJson);
        String str= (String) JSON.parse(userJson);
        System.out.println(str);
        SysUser sysUser=JSON.parseObject(str,SysUser.class);
        return  sysUser;
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/user/admin.png");
            sysUser.setNickname("lhclike");
        }
        UserVo userVo = new UserVo();
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
        userVo.setId(sysUser.getId());
        return userVo;
    }
}