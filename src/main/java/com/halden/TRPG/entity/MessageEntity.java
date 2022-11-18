package com.halden.TRPG.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("message")
public class MessageEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String uid;

    private Date createTime;

    private String message;

    private String username;

    private Long rid;
}
