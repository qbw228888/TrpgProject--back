package com.halden.TRPG.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SystemMessageResponseVo {
    private Date date;
    private String title;
    private String message;
    private boolean isMine;
    private String messageType;
    private Long rid;
}
