package com.lhclike.myblog.vo;

public enum ErrorCode {
    PARAMS_ERROR(10001,"参数有误"),
    ACCOUNT_PWD_NOT_EXIST(10002,"用户名或密码不存在"),
    NO_PERMISSION(70001,"无访问权限"),
    SESSION_TIME_OUT(90001,"会话超时"),
    NO_LOGIN(90002,"未登录"),
    ACCOUNT_EXIST(10004,"用户名已存在"),
    PHONENUMBER_CODE_NULL(10005,"电话或验证码为空"),
    INVALID_PHONENUMBER(10006,"无效的手机号"),
    CODE_ERROR(10007,"验证码错误");


    private int code;
    private String msg;

    ErrorCode(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
