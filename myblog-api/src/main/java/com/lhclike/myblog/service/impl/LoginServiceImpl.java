package com.lhclike.myblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.lhclike.myblog.config.JWTUtils;
import com.lhclike.myblog.pojo.SysUser;
import com.lhclike.myblog.service.LoginService;
import com.lhclike.myblog.service.UserService;
import com.lhclike.myblog.vo.ErrorCode;
import com.lhclike.myblog.vo.LoginParam;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.params.LoginBySmsParam;
import com.lhclike.myblog.vo.params.LoginSendPhonenumber;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;

@Transactional
@Service
public class LoginServiceImpl implements LoginService {

    private static final String slat="lhclike";

    @Autowired
    private UserService UserService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result login(LoginParam loginParam) {
        String account=loginParam.getAccount();
        String password=loginParam.getPassword();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return  Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        String pwd= DigestUtils.md5Hex(password+slat);
        SysUser sysUser=UserService.findUser(account,pwd);
        if(sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token= JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        System.out.println("TOKEN_"+token);
        return  Result.success(token);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        String account =loginParam.getAccount();
        String password=loginParam.getPassword();
        String nickname=loginParam.getNickname();
        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)||StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());}
            SysUser sysUser=this.UserService.findUserByAccount(account);
            if (sysUser != null){
                return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
            }
            sysUser = new SysUser();
            sysUser.setNickname(nickname);
            sysUser.setAccount(account);
            sysUser.setPassword(DigestUtils.md5Hex(password+slat));
            sysUser.setCreateDate(System.currentTimeMillis());
            sysUser.setLastLogin(System.currentTimeMillis());
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setAdmin(1); //1 为true
            sysUser.setDeleted(0); // 0 为false
            sysUser.setSalt("");
            sysUser.setStatus("");
            sysUser.setEmail("");
            this.UserService.save(sysUser);
            //token
            String token = JWTUtils.createToken(sysUser.getId());

            redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
            return Result.success(token);
        }

    @Override
    public SysUser checkToken(String token) {
        return UserService.checkToken(token);
    }

    // 校验中国大陆手机号
    public static boolean isValidChineseMobileNumber(String mobileNumber) {
        String regex = "^1[3-9]\\d{9}$";
        return mobileNumber.matches(regex);
    }

    @Override
    public Result loginBySms(LoginBySmsParam loginBySmsParam) {

        String phonenumber=loginBySmsParam.getPhoneNumber();
        String code=loginBySmsParam.getCode();
        if(StringUtils.isBlank(phonenumber)||StringUtils.isBlank(code)){
            return Result.fail(ErrorCode.PHONENUMBER_CODE_NULL.getCode(), ErrorCode.PHONENUMBER_CODE_NULL.getMsg());}
        //校验手机号码
        if(!isValidChineseMobileNumber(phonenumber)){
            return Result.fail(ErrorCode.INVALID_PHONENUMBER.getCode(), ErrorCode.INVALID_PHONENUMBER.getMsg());
        }
        LoginBySmsParam smsParam= (LoginBySmsParam) redisTemplate.opsForValue().get("CODE_"+phonenumber);


        //校验验证码
        if(phonenumber.equals(smsParam.getPhoneNumber()) && code.equals(smsParam.getCode())){
            return Result.success("验证成功");
        }


        return  Result.fail(ErrorCode.CODE_ERROR.getCode(), ErrorCode.CODE_ERROR.getMsg());



    }

    @Transactional
    @Override
    public Result loginSendSms(LoginSendPhonenumber loginSendPhonenumber) {
        String phonenumber=loginSendPhonenumber.getPhoneNumber();
        System.out.println(phonenumber);
        Map<String,Object> map=new HashMap<>();
        String code=generateCode(6);
        map.put("code",code);
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI5tLALUgqtpHsaitP9NMY", "gOJvZVarzJmZYgAwXjN3yeNzF0kaeF");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phonenumber);
        request.putQueryParameter("SignName", "lhclike");
        request.putQueryParameter("TemplateCode", "SMS_464115503");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        LoginBySmsParam loginBySmsResult=new LoginBySmsParam(phonenumber,code);
        redisTemplate.opsForValue().set("CODE_"+phonenumber, loginBySmsResult,1, TimeUnit.DAYS);
//        redisTemplate.opsForValue().set("CODE_"+phonenumber, loginBySmsParam,60, TimeUnit.SECONDS);
        return Result.success("短信发送成功");
    }


    public static String generateCode(int length) {
            // 使用SecureRandom来增强验证码的安全性
            SecureRandom random = new SecureRandom();

            StringBuilder code = new StringBuilder();

            for (int i = 0; i < length; i++) {
                // 生成单个数字并添加到验证码字符串
                code.append(random.nextInt(10)); // 生成0-9之间的数字
            }

            return code.toString();
        }
    public static void main(String[] args) {
            // 生成6位数的验证码
            String smsCode = generateCode(6);
            System.out.println("验证码: " + smsCode);
        }







//    public static void main(String[] args) {
//        System.out.println(DigestUtils.md5Hex("admin"+slat));
//    }
}
