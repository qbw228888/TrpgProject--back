package com.halden.TRPG.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("system_message")
public class SystemMessageEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String message;

    private String uid;

    private Date createTime;

    private Boolean isViewed;

    private Boolean isMine;

    private String messageType;

    private Long rid;
}
