package com.lhclike.myblog.api;

import com.lhclike.myblog.service.LoginService;
import com.lhclike.myblog.vo.LoginParam;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.params.LoginBySmsParam;
import com.lhclike.myblog.vo.params.LoginSendPhonenumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;



    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }

    @PostMapping("/sms")
    public Result loginBySms(@RequestBody LoginBySmsParam loginBySmsParam){
        System.out.println(loginBySmsParam.getSmsCode()+"dadadadadada"+loginBySmsParam.getPhoneNumber());
        return loginService.loginBySms(loginBySmsParam);
    }
    @CrossOrigin
    @PostMapping("/sendsms")
    public Result loginSendSms(@RequestBody LoginSendPhonenumber loginSendPhonenumber){

        System.out.println("dddddddddddddddddddddddddddddddddddddddddddsaaaadddddddddddddddddddddddddddddddddddddddddd");

        return loginService.loginSendSms(loginSendPhonenumber);
    }
   /* @PostMapping("/sendsms")
    public  Result loginSenSms(@RequestParam String phonenumber ){
        System.out.println(phonenumber);
        return loginService.loginSendSms(new LoginSendPhonenumber());
    }*/



}
