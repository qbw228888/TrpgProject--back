package com.halden.TRPG.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SystemMessageSendVo {
    private String uid;
    private Date createTime;
    private String title = "system message send by user";
    private String message;
}
