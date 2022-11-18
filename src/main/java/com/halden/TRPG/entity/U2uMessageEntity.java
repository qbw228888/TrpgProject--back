package com.halden.TRPG.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("u2u_message")
public class U2uMessageEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String sendUid;

    private String acceptUid;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
