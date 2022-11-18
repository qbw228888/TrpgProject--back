package com.halden.TRPG.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("apply_add_room")
public class ApplyAddRoomEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String applierUid;

    private Long rid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
