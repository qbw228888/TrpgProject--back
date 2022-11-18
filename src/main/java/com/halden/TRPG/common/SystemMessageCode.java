package com.halden.TRPG.common;

public enum SystemMessageCode {

    NORMALMESSAGE("0");

    String code;

    SystemMessageCode(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
