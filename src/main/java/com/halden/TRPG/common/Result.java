package com.halden.TRPG.common;

import lombok.Data;

import java.util.HashMap;

public class Result extends HashMap<String,Object> {


    public static Result ok(int code, String message){
        Result result = new Result();
        result.put("code",code);
        result.put("message",message);
        return result;
    }

    public static Result ok(int code){
        Result result = new Result();
        result.put("code",code);
        return result;
    }

    public static Result error(int code, String message){
        Result result = new Result();
        result.put("code",code);
        result.put("message",message);
        return result;
    }

    public Result add(String key, Object value){
        super.put(key,value);
        return this;
    }
}
