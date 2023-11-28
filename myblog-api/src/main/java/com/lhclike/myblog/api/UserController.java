package com.lhclike.myblog.api;


import com.lhclike.myblog.service.UserService;
import com.lhclike.myblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("currentUser")
    //请求头中的token信息赋值给控制器token变量
    public Result currentUser(@RequestHeader("Authorization") String token){
        return userService.getUserInfoByToken(token);
    }
}
