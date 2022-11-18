package com.halden.TRPG.entity.vo;

import lombok.Data;

@Data
public class MessageSendVo {
    private String uid;
    private Long rid;
    private String message;
    private String username;
}
