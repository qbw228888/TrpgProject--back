package com.halden.TRPG.common;

import javax.xml.crypto.Data;

public enum CodeEnum {
    SUCCESS(200,"操作成功"),
    UNKNOWN_EXCEPTION(100000,"系统未知异常"),
    ERROR_PARAMETER_EXCEPTION(100001,"传入了错误或无效的参数"),
    VAILD_EXCEPTION(100002,"参数格式校验失败"),
    TO_MANY_REQUEST(100003,"请求流量过大，请稍后再试"),
    OPTION_FAIL_EXCEPTION(100004,"操作失败"),
    LOGIN_ACCT_PASSWORD_EXCEPTION(101000,"账号或密码错误"),
    PHONE_HAS_BEEN_REGISTED_EXCEPTHION(101001,"手机号已经被注册，请登录"),
    NO_SUCH_USER_EXCEPTION(101002,"该用户不存在"),
    ROOM_FULL_EXCEPTION(102000,"房间已满"),
    NOT_OWNER_EXCEPTION(102001,"非房主没有权限完成此操作"),
    ALREADY_IN_EXCEPTION(102002,"已经加入该房间，不可重复加入"),
    ALREADY_APPLIED_JOIN_ROOM_EXCEPTION(102003,"已经申请过加入该房间，不可重复加入"),
    NO_APPLY_EXCEPTION(102004,"用户没有加入房间的请求"),
    ALREADY_EXIST_EXCEPTION(103000,"数据已经存在");

    private Integer code;

    private String message;

    private Object data;

    CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData(){
        return data;
    }

    public CodeEnum setData(Object data){
        this.data = data;
        return this;
    }
}
